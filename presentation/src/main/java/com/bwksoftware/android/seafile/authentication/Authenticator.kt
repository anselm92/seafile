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

package com.bwksoftware.android.seafile.authentication

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.bwksoftware.android.seafile.BuildConfig
import com.bwksoftware.android.seafile.view.activity.AccountActivity
import javax.inject.Inject


class Authenticator @Inject constructor(val mContext: Context) : AbstractAccountAuthenticator(
        mContext) {
    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?,
                                    options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?,
                                   authTokenType: String?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?,
                              authTokenType: String?, options: Bundle?): Bundle {
        val am = AccountManager.get(mContext)

        var authToken = am.peekAuthToken(account, authTokenType)

        if (TextUtils.isEmpty(authToken)) {
            //authToken = HTTPNetwork.login(account?.name, am.getPassword(account))
        }


        if (!TextUtils.isEmpty(authToken)) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }

        val intent = Intent(mContext, AccountActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(BuildConfig.APPLICATION_ID, account?.type)

        val retBundle = Bundle()
        retBundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return retBundle
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?,
                             features: Array<out String>?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editProperties(response: AccountAuthenticatorResponse?,
                                accountType: String?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?,
                            authTokenType: String?, requiredFeatures: Array<out String>?,
                            options: Bundle?): Bundle {
        val intent = Intent(mContext, AccountActivity::class.java)
        // This key can be anything. Try to use your domain/package
        intent.putExtra(BuildConfig.APPLICATION_ID, accountType)
        // Copy this exactly from the line below.
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getCurrentUserAuthToken(accountName:String, activity: Activity): String {
        val account : Account = Account(accountName, mContext.packageName)

        return AccountManager.get(mContext).peekAuthToken(account,"full_access")

//        val accountFuture = SeafAccountManager.get(mContext).getAuthToken(account, "ah", null, activity,
//                null, null)
//        val authTokenBundle = accountFuture.result
//        val authToken = authTokenBundle.get(SeafAccountManager.KEY_AUTHTOKEN)!!.toString()
//        return authToken
    }

}