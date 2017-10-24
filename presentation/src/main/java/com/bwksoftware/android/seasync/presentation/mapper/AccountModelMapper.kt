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

package com.bwksoftware.android.seasync.presentation.mapper

import android.view.MenuItem
import com.bwksoftware.android.seasync.domain.AccountTemplate
import com.bwksoftware.android.seasync.presentation.model.NavButton
import javax.inject.Inject
import android.accounts.Account as AndroidAccount
import com.bwksoftware.android.seasync.presentation.model.Account as SeafAccount


class AccountModelMapper @Inject constructor() {

    fun transformAccount(account: AndroidAccount): SeafAccount {
        return SeafAccount("", account.name, "")
    }

    fun transformAccounts(accounts: List<AndroidAccount>): List<SeafAccount> {
        return accounts.mapTo(ArrayList()) { transformAccount(it) }
    }

    fun transformToAccount(account: AccountTemplate): SeafAccount {
        return SeafAccount(account.token, account.username, account.imageUrl)
    }

    fun transformToAccounts(accounts: List<AccountTemplate>): List<SeafAccount> {
        return accounts.mapTo(ArrayList()) { transformToAccount(it) }
    }


    fun transformToAccountTemplate(account: SeafAccount): AccountTemplate {
        return AccountTemplate(account.token, account.name, account.imgLink)
    }

    fun transformToAccountTemplates(accounts: List<SeafAccount>): List<AccountTemplate> {
        return accounts.mapTo(ArrayList()) { transformToAccountTemplate(it) }
    }

    fun transformMenuItem(menu: MenuItem): NavButton {
        return NavButton(menu.title.toString(), menu.icon, menu.itemId)
    }

    fun transformMenuItems(menus: List<MenuItem>): List<NavButton> {
        return menus.mapTo(ArrayList()) { transformMenuItem(it) }
    }
}