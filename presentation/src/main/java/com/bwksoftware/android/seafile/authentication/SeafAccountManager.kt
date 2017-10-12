package com.bwksoftware.android.seafile.authentication

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import com.bwksoftware.android.seafile.BuildConfig
import com.bwksoftware.android.seafile.prefs.SharedPrefsController
import javax.inject.Inject


class SeafAccountManager @Inject constructor(val authenticator: Authenticator,
                                             val context: Context,
                                             val sharedPrefsController: SharedPrefsController) {

    fun createAccount(email: String, password: String, serverAddress: String,
                              authToken: String): Account {
        val account = Account(email, context.packageName)
        val am = AccountManager.get(context)
        am.addAccountExplicitly(account, password, null)
        am.setAuthToken(account, "full_access", authToken)
        am.setUserData(account, "Server", serverAddress)
        return account
    }

    fun getAllAccounts(): List<Account> {
        val accountManager = AccountManager.get(context)
        val currentAccountName = sharedPrefsController.getPreference(
                SharedPrefsController.Preference.CURRENT_USER_ACCOUNT)
        var accounts = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID)
        if(accounts.isEmpty())
            accounts+=Account("None", "full_access")
        var accountsOrdered = accounts.sortedWith(kotlin.Comparator { o1, _ ->
            if (o1.name == currentAccountName) -1 else 0
        })
        return accountsOrdered
    }

    fun getAccountByName(accountName: String): Account? {

        val accounts = getAllAccounts()
        var currentAndroidAccount: Account? = null
        accounts.filter { it.name == accountName }
                .forEach { currentAndroidAccount = it }
        return currentAndroidAccount
    }

    fun getCurrentAccount(): Account {
        var account = getAccountByName(sharedPrefsController.getPreference(
                SharedPrefsController.Preference.CURRENT_USER_ACCOUNT))
        if(account==null){
            account = Account("None","full_access")
        }
        return account
    }

    fun getCurrentAccountToken(): String {
        val currentAccountName = sharedPrefsController.getPreference(
                SharedPrefsController.Preference.CURRENT_USER_ACCOUNT)
        val currentAccount = getAccountByName(currentAccountName)
        if (currentAccount != null && currentAccount.name!="None") {
            return AccountManager.get(context).peekAuthToken(currentAccount, "full_access")
        } else {
            return ""
        }
    }


}