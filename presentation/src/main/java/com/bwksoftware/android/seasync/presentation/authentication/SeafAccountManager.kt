package com.bwksoftware.android.seasync.presentation.authentication

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.util.Log
import com.bwksoftware.android.seasync.data.prefs.SharedPrefsController
import com.bwksoftware.android.seasync.presentation.BuildConfig
import com.bwksoftware.android.seasync.presentation.R
import javax.inject.Inject


class SeafAccountManager @Inject constructor(val authenticator: Authenticator,
                                             val context: Context,
                                             val sharedPrefsController: SharedPrefsController) {

    fun createAccount(email: String, password: String, serverAddress: String,
                      authToken: String): Account {
        val account = Account(email, context.getString(R.string.authtype))
        Log.d("SeafAccMgr",account.toString())
        val am = AccountManager.get(context)
        ContentResolver.setIsSyncable(account,context.getString(R.string.authtype),1)
        ContentResolver.setSyncAutomatically(account,context.getString(R.string.authtype), true);
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
        if (accounts.isEmpty())
            accounts += Account("None", context.getString(R.string.authtype))
        return accounts.sortedWith(Comparator { o1, _ ->
            if (o1.name == currentAccountName) -1 else 0
        })
    }

    fun getAccountByName(accountName: String): Account? {

        val accounts = getAllAccounts()
        var currentAndroidAccount: Account? = null
        accounts.filter { it.name == accountName }
                .forEach { currentAndroidAccount = it }
        return currentAndroidAccount
    }

    fun getServerAddress(account: Account): String? {
        return AccountManager.get(context).getUserData(account, "Server")
    }

    fun getCurrentAccount(): Account {
        var account = getAccountByName(sharedPrefsController.getPreference(
                SharedPrefsController.Preference.CURRENT_USER_ACCOUNT))
        if (account == null) {
            account = Account("None",  context.getString(R.string.authtype))
        }
        return account
    }

    fun getCurrentAccountToken(): String {
        val currentAccountName = sharedPrefsController.getPreference(
                SharedPrefsController.Preference.CURRENT_USER_ACCOUNT)
        val currentAccount = getAccountByName(currentAccountName)
        if (currentAccount != null && currentAccount.name != "None") {
            return AccountManager.get(context).peekAuthToken(currentAccount, "full_access")
        } else {
            return ""
        }
    }


}