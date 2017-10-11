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

package com.bwksoftware.android.seafile.mapper

import android.view.Menu
import android.view.MenuItem
import com.bwksoftware.android.seafile.model.NavButton
import javax.inject.Inject
import com.bwksoftware.android.seafile.model.Account as SeafAccount
import android.accounts.Account as AndroidAccount

/**
 * Created by ansel on 10/11/2017.
 */
class AccountModelMapper @Inject constructor() {

    fun transformAccount(account: AndroidAccount): SeafAccount {
        val img = if (account.name.contains("gmail")) "https://cloud.bwk-technik.de/thumbnail/7d82f01ced9f40fb8226/128/IMAG1747%20(2).jpg"
        else "https://cloud.bwk-technik.de/thumbnail/7d82f01ced9f40fb8226/1024/IMAG1760.jpg"
        return SeafAccount(account.name, img )
    }

    fun transformAccounts(accounts: List<AndroidAccount>): List<SeafAccount> {
        return accounts.mapTo(ArrayList()) { transformAccount(it) }
    }

    fun transformMenuItem(menu: MenuItem): NavButton {
        return NavButton(menu.title.toString(), menu.icon, menu.itemId)
    }

    fun transformMenuItems(menus: List<MenuItem>): List<NavButton> {
        return menus.mapTo(ArrayList()) { transformMenuItem(it) }
    }
}