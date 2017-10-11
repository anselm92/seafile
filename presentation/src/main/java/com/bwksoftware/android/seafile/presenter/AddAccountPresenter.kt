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
import android.content.Context
import android.util.Log
import com.bwksoftware.android.seafile.authentication.Authenticator
import com.bwksoftware.android.seafile.domain.AccountTemplate
import com.bwksoftware.android.seafile.domain.interactor.DefaultObserver
import com.bwksoftware.android.seafile.domain.interactor.GetAccountToken
import com.bwksoftware.android.seafile.mapper.AccountModelMapper
import com.bwksoftware.android.seafile.view.views.AddAccountView
import javax.inject.Inject


class AddAccountPresenter @Inject constructor(private val getAccountToken: GetAccountToken,
                                              val context: Context,
                                              private val accountMapper: AccountModelMapper) {

    internal lateinit var accountView: AddAccountView
    @Inject lateinit var authenticator: Authenticator

    fun createNewAccount(username: String, password: String, serverAddress: String) {
        this.getAccountToken.execute(AccountTokenObserver(username, password, serverAddress),
                GetAccountToken.Params(username, password))
    }


    private fun createAccount(email: String, password: String, serverAddress: String,
                              authToken: String): Account {
        val account = Account(email, context.packageName)
        val am = AccountManager.get(context)
        am.addAccountExplicitly(account, password, null)
        am.setAuthToken(account, "full_access", authToken)
        am.setUserData(account, "Server", serverAddress)
        return account
    }

    private inner class AccountTokenObserver(val username: String,
                                             val password: String,
                                             val serverAddress: String) : DefaultObserver<AccountTemplate>() {

        override fun onComplete() {
            Log.d("AccountPresenter", "yolo complete")
        }

        override fun onError(exception: Throwable) {
            accountView.onCreateUnsuccessful()
            Log.d("AccountPresenter", "yolo error" + exception.localizedMessage)
        }

        override fun onNext(account: AccountTemplate) {
            Log.d("AccountPresenter", account.token)

            val account = createAccount(username, password, serverAddress, account.token)
            accountView.onCreateSuccessful(account)
        }
    }

}