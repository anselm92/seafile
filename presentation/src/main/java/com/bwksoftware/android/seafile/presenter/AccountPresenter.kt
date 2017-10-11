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
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.bwksoftware.android.seafile.BuildConfig
import com.bwksoftware.android.seafile.domain.AccountTemplate
import com.bwksoftware.android.seafile.domain.interactor.DefaultObserver
import com.bwksoftware.android.seafile.domain.interactor.GetAccountToken
import com.bwksoftware.android.seafile.internal.di.scope.PerActivity
import com.bwksoftware.android.seafile.mapper.AccountModelMapper
import com.bwksoftware.android.seafile.model.NavBaseItem
import com.bwksoftware.android.seafile.prefs.SharedPrefsController
import com.bwksoftware.android.seafile.view.views.AccountView
import java.util.*
import javax.inject.Inject
import com.bwksoftware.android.seafile.model.Account as SeafAccount


@PerActivity
class AccountPresenter @Inject constructor(private val getAccountToken: GetAccountToken,
                                           val context: Context,
                                           private val accountMapper: AccountModelMapper,
                                           val prefsController: SharedPrefsController) {
    lateinit var currentAccount: Account
    lateinit var view: AccountView
    var showsAccounts: Boolean = false


    fun showNavList(menu: Menu) {
        showsAccounts = false
        val items: MutableList<NavBaseItem> = (0 until menu.size()).mapTo(
                LinkedList()) { accountMapper.transformMenuItem(menu.getItem(it)) }
        items.add(0, getCurrentAccount())
        view.showNavList(items)
    }

    fun showAccountList(menu: Menu) {
        showsAccounts = true
        val items: MutableList<NavBaseItem> = (0 until menu.size()).mapTo(
                LinkedList()) { accountMapper.transformMenuItem(menu.getItem(it)) }
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID)
        val currentAccountName = prefsController.getPreference(
                SharedPrefsController.Preference.CURRENT_USER_ACCOUNT)
        val accountsOrdered = accounts.sortedWith(kotlin.Comparator { o1, _ ->
            if (o1.name == currentAccountName) -1 else 0
        })
        view.showAccounts(accountMapper.transformAccounts(accountsOrdered) + items)
    }

    private fun getCurrentAccount(): SeafAccount {
        val currentAccountName = prefsController.getPreference(
                SharedPrefsController.Preference.CURRENT_USER_ACCOUNT)
        return accountMapper.transformAccount(Account(currentAccountName, "full_access"))
    }


    fun createNewAccount(username: String, password: String) {
//        val accountManager = AccountManager.get(context)
//        val accounts = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID)
//        val account = Account(username,"full_access")
//        if (accounts.isNotEmpty() && accounts.contains(account)) {
//            //accountManager.getAuthToken(accounts[0], "full_access", false, GetAuthTokenCallback(), null)
//            //return
//        }
        this.getAccountToken.execute(AccountTokenObserver(username, password),
                GetAccountToken.Params(username, password))
    }

    fun selectAccount(account: SeafAccount) {
        //val accountManager = AccountManager.get(context)
        prefsController.setPreference(SharedPrefsController.Preference.CURRENT_USER_ACCOUNT,
                account.name)

        currentAccount = Account(account.name, "full_access")
        view.selectAccount(account)
        //accountManager.getAuthToken(account, "full_access", false, GetAuthTokenCallback(), null)
    }

    fun createAccount(email: String, password: String, authToken: String) {
        val account = Account(email, context.packageName)
        val am = AccountManager.get(context)
        am.addAccountExplicitly(account, password, null)
        am.setAuthToken(account, "full_access", authToken)
        selectAccount(SeafAccount(account.name, ""))
    }


    private inner class AccountTokenObserver(val username: String,
                                             val password: String) : DefaultObserver<AccountTemplate>() {

        override fun onComplete() {
            Log.d("AccountPresenter", "yolo complete")
        }

        override fun onError(exception: Throwable) {
            Log.d("AccountPresenter", "yolo error" + exception.localizedMessage)
        }

        override fun onNext(account: AccountTemplate) {
            Log.d("AccountPresenter", account.token)

            createAccount(username, password, account.token)
            view.refreshAccountList()
        }
    }

    private inner class GetAuthTokenCallback : AccountManagerCallback<Bundle> {
        override fun run(result: AccountManagerFuture<Bundle>) {
            val bundle: Bundle
            try {
                bundle = result.result
                val intent = bundle.get(AccountManager.KEY_INTENT)
                if (intent != null) {
                    // User input required
                    //startActivity(intent)
                } else {
                    Log.d("AccountPresenter", bundle.getString(AccountManager.KEY_AUTHTOKEN))
                    prefsController.setPreference(
                            SharedPrefsController.Preference.CURRENT_USER_TOKEN,
                            bundle.getString(AccountManager.KEY_AUTHTOKEN))
                }
            } catch (e: Exception) {
                Log.d("AccountPresenter", e.message)
            }

        }
    }

}