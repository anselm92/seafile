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

package com.bwksoftware.android.seafile.view.fragment

import android.accounts.Account
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.AppCompatButton
import android.view.View
import android.widget.Toast
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.mapper.AccountModelMapper
import com.bwksoftware.android.seafile.presenter.AddAccountPresenter
import com.bwksoftware.android.seafile.view.views.AddAccountView
import javax.inject.Inject
import com.bwksoftware.android.seafile.model.Account as SeafAccount


class AddAccountFragment : BaseFragment(), AddAccountView {

    interface OnAddAccountListener {
        fun onAccountComplete(account: SeafAccount)
    }

    companion object {
        private const val PARAM_ACCOUNT = "param_account"

        fun forEmptyAccount(): AddAccountFragment {
            return AddAccountFragment()
        }

        fun forAccount(account: Account): AddAccountFragment {
            val addAccountFragment = AddAccountFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_ACCOUNT, account.name)
            addAccountFragment.arguments = arguments
            return addAccountFragment
        }
    }

    @Inject lateinit var addAccountPresenter: AddAccountPresenter
    @Inject lateinit var accountModelMapper: AccountModelMapper

    lateinit var emailInput: TextInputEditText
    lateinit var passwordInput: TextInputEditText
    lateinit var serverAddressInput: TextInputEditText
    lateinit var addAccountButton: AppCompatButton

    override fun layoutId() = R.layout.fragment_create_account

    override fun activity() = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailInput = view!!.findViewById(R.id.username)
        passwordInput = view.findViewById(R.id.password)
        serverAddressInput = view.findViewById(R.id.server_address)
        addAccountButton = view.findViewById(R.id.add_account)

        if (firstTimeCreated(savedInstanceState)) {
            initializeView()
            loadAccountDetails()
        }
    }


    override fun onCreateSuccessful(account: Account) {
        val attachedActivity = activity
        when (attachedActivity) {
            is OnAddAccountListener -> attachedActivity.onAccountComplete(
                    accountModelMapper.transformAccount(account))
        }
    }

    override fun onCreateUnsuccessful() {
        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
    }

    private fun loadAccountDetails() {
        if (arguments != null) {
            //TODO: if so read account stuff from authenticator
        }
    }

    private fun initializeView() {
        addAccountPresenter.accountView = this
        addAccountButton.setOnClickListener({ createAccount() })
    }

    private fun createAccount() {
        addAccountPresenter.createNewAccount(emailInput.text.toString(),
                passwordInput.text.toString(),
                serverAddressInput.text.toString())
    }


}