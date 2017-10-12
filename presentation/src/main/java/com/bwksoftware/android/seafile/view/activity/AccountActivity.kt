/*
 *    Copyright 2018 BWK Technik GbR
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.bwksoftware.android.seafile.view.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.bwksoftware.android.seafile.App
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.internal.di.components.ActivityComponent
import com.bwksoftware.android.seafile.internal.di.components.DaggerActivityComponent
import com.bwksoftware.android.seafile.internal.di.modules.ActivityModule
import com.bwksoftware.android.seafile.model.Account
import com.bwksoftware.android.seafile.model.NavBaseItem
import com.bwksoftware.android.seafile.navigation.Navigator
import com.bwksoftware.android.seafile.presenter.AccountPresenter
import com.bwksoftware.android.seafile.view.adapter.AccountAdapter
import com.bwksoftware.android.seafile.view.fragment.AddAccountFragment
import com.bwksoftware.android.seafile.view.fragment.BaseFragment
import com.bwksoftware.android.seafile.view.fragment.DirectoryFragment
import com.bwksoftware.android.seafile.view.fragment.ReposFragment
import com.bwksoftware.android.seafile.view.views.AccountView
import javax.inject.Inject
import android.accounts.Account as AndroidAccount




class AccountActivity : AppCompatActivity(), AccountView, AccountAdapter.OnItemClickListener, AddAccountFragment.OnAddAccountListener, ReposFragment.OnRepoClickedListener, DirectoryFragment.OnDirectoryClickedListener {

    lateinit private var navRecyclerView: RecyclerView
    lateinit private var toolbar: Toolbar
    lateinit private var drawerLayout: DrawerLayout
    lateinit private var accountAdapter: AccountAdapter
    lateinit private var navMenu: Menu

    private var reposFragment: ReposFragment? = null

    @Inject
    lateinit var presenter: AccountPresenter
    @Inject
    lateinit var navigator: Navigator

    private val component: ActivityComponent
        get() = DaggerActivityComponent.builder()
                .applicationComponent((application as App).component)
                .activityModule(ActivityModule(this))
                .build()


    val Activity.app: App
        get() = application as App

    override fun activity() = this

    override fun onHeaderButtonClicked() = if (presenter.showsAccounts) {
        navMenu.clear()
        menuInflater.inflate(R.menu.nav_menu, navMenu)
        presenter.showNavList(navMenu)
    } else {
        navMenu.clear()
        menuInflater.inflate(R.menu.nav_menu_accounts, navMenu)
        presenter.showAccountList(navMenu)
    }

    override fun onRepoClicked(fragment: BaseFragment, repoId: String, repoName: String) {
        navigator.navigateToDirectory(this, fragment.childFragmentManager, presenter.currentAccount,
                repoId,repoName,"")
    }

    override fun onDirectoryClicked(fragment: BaseFragment, repoId: String,repoName: String, directory: String) {
        navigator.navigateToDirectory(this, fragment.childFragmentManager, presenter.currentAccount,
                repoId,repoName,directory)    }

    override fun onFileClicked(fragment: BaseFragment, repoId: String,repoName: String, file: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onButtonClicked(itemId: Int) {
        when (itemId) {
            R.id.repos -> {
                navigator.navigateToReposView(this, supportFragmentManager,
                        presenter.currentAccount)
            }
            R.id.uploads -> {
                navigator.navigateToUploadsView(this, supportFragmentManager)
            }
            R.id.add_account -> {
                navigator.navigateToAddAccountView(this, supportFragmentManager)
            }
        }
        drawerLayout.closeDrawer(Gravity.START)

        Toast.makeText(app.baseContext, navMenu.findItem(itemId).title,
                Toast.LENGTH_SHORT).show()
    }

    override fun onAccountClicked(account: Account) {
        navMenu.clear()
        menuInflater.inflate(R.menu.nav_menu, navMenu)
        presenter.selectAccount(account)
        presenter.showNavList(navMenu)
    }

    override fun showNavList(items: List<NavBaseItem>) {
        accountAdapter.setItems(items)
        accountAdapter.notifyDataSetChanged()
    }

    override fun selectAccount(account: Account) {
        initNavigationDrawer()

    }

    override fun onAccountComplete(account: Account) {
        refreshAccountList()
        onAccountClicked(account)
        supportFragmentManager.popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)
        accountAdapter = AccountAdapter(this, this)
        presenter.view = this
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            initNavigationDrawer()
            presenter.init()
            initScreen()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)
        navMenu = menu!!
        presenter.showNavList(navMenu)
        return true
    }

    fun setTitle(title: String){
        toolbar.title = title
    }

    private fun initNavigationDrawer() {
        navRecyclerView = findViewById(R.id.nav_menu__rvlist)
        with(navRecyclerView) {
            this.layoutManager = LinearLayoutManager(this.context)
            this.setAdapter(accountAdapter)
        }
        drawerLayout = findViewById<View>(R.id.drawer) as DrawerLayout
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {
        }
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun initScreen(){
        if(presenter.currentAccount.name!="None") {
            reposFragment = ReposFragment.forAccount(presenter.currentAccount)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, reposFragment,ReposFragment::class.java.name)
                    .commit()
            setTitle(reposFragment!!.name())
        }
    }

    private var doubleBackToExitPressedOnce: Boolean = false

    private var onBackPressedOnMainFragmentPressedOnce: Boolean = false

    override fun onBackPressed() {

        if (!getActiveFragment()?.onBackPressed()!!) {
            if (!onBackPressedOnMainFragmentPressedOnce) {
                onBackPressedOnMainFragmentPressedOnce = true
                return
            }
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click back again to logout", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

        } else {
            // carousel handled the back pressed task
            // do not call super
        }
    }

    fun getActiveFragment(): BaseFragment? {
        if (supportFragmentManager.backStackEntryCount == 0) {
            return reposFragment
        }
        val tag = supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1).name
        return supportFragmentManager.findFragmentByTag(tag) as BaseFragment?
    }


}
