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

package com.bwksoftware.android.seafile.presenter

import android.accounts.Account
import android.content.Context
import android.util.Log
import android.view.Menu
import com.bwksoftware.android.seafile.authentication.SeafAccountManager
import com.bwksoftware.android.seafile.domain.AccountTemplate
import com.bwksoftware.android.seafile.domain.interactor.DefaultObserver
import com.bwksoftware.android.seafile.domain.interactor.GetAllAccounts
import com.bwksoftware.android.seafile.internal.di.scope.PerActivity
import com.bwksoftware.android.seafile.mapper.AccountModelMapper
import com.bwksoftware.android.seafile.model.NavBaseItem
import com.bwksoftware.android.seafile.prefs.SharedPrefsController
import com.bwksoftware.android.seafile.view.views.AccountView
import java.util.*
import javax.inject.Inject
import com.bwksoftware.android.seafile.model.Account as SeafAccount


@PerActivity
class AccountPresenter @Inject constructor(
        private val getAllAccounts: GetAllAccounts,
        val context: Context,
        private val seafAccountManager: SeafAccountManager,
        private val accountMapper: AccountModelMapper,
        val prefsController: SharedPrefsController) {

    lateinit var currentAccount: Account
    lateinit var view: AccountView
    var showsAccounts: Boolean = false

    fun init() {
        currentAccount = seafAccountManager.getCurrentAccount()
    }

    fun showNavList(menu: Menu) {
        showsAccounts = false
        val items: MutableList<NavBaseItem> = (0 until menu.size()).mapTo(
                LinkedList()) { accountMapper.transformMenuItem(menu.getItem(it)) }
        val currentAccount = seafAccountManager.getCurrentAccount()
        val seafAccount: SeafAccount = accountMapper.transformAccount(currentAccount)

        val authToken = seafAccountManager.getCurrentAccountToken()
        getAllAccounts.execute(AccountsObserver(items), GetAllAccounts.Params(authToken,
                accountMapper.transformToAccountTemplates(listOf(seafAccount))))
    }

    fun showAccountList(menu: Menu) {
        showsAccounts = true
        val items: MutableList<NavBaseItem> = (0 until menu.size()).mapTo(
                LinkedList()) { accountMapper.transformMenuItem(menu.getItem(it)) }

        val seafAccounts = accountMapper.transformAccounts(seafAccountManager.getAllAccounts())
        val authToken = seafAccountManager.getCurrentAccountToken()

        getAllAccounts.execute(AccountsObserver(items), GetAllAccounts.Params(authToken,
                accountMapper.transformToAccountTemplates(seafAccounts)))

    }


    fun selectAccount(account: SeafAccount) {
        prefsController.setPreference(SharedPrefsController.Preference.CURRENT_USER_ACCOUNT,
                account.name)
        currentAccount = Account(account.name, "full_access")
        view.selectAccount(account)
    }

    private inner class AccountsObserver(
            val otherItems: List<NavBaseItem>) : DefaultObserver<List<AccountTemplate>>() {
        override fun onError(exception: Throwable) {
            Log.d("AccountPresenter", "yolo error" + exception.localizedMessage)
            view.showNavList(listOf(SeafAccount("","","None")) + otherItems)
        }

        override fun onNext(accounts: List<AccountTemplate>) {
            Log.d("yolo", accounts.toString())
            view.showNavList(accountMapper.transformToAccounts(accounts) + otherItems)
        }
    }


}