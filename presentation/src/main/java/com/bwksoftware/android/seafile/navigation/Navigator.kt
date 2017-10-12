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

package com.bwksoftware.android.seafile.navigation

import android.accounts.Account
import android.content.Context
import android.support.v4.app.FragmentManager
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.view.fragment.AddAccountFragment
import com.bwksoftware.android.seafile.view.fragment.DirectoryFragment
import com.bwksoftware.android.seafile.view.fragment.ReposFragment
import com.bwksoftware.android.seafile.view.fragment.UploadsFragment
import javax.inject.Inject

class Navigator @Inject constructor() {

    fun navigateToDirectory(context: Context, fragmentManager: FragmentManager, account: Account,
                            repoId: String,directory: String) {

        val directoryFragment = DirectoryFragment.forAccountRepoAndDir(account,repoId,directory)

        val transaction = fragmentManager.beginTransaction()
        // Store the Fragment in stack
        transaction.addToBackStack(DirectoryFragment::class.java.name)
        //        transaction.setCustomAnimations(R.anim.enter_from_center,R.anim.exit_from_center);
        transaction.replace(R.id.container, directoryFragment,
                DirectoryFragment::class.java.name).commit()
    }

    fun navigateToReposView(context: Context, fragmentManager: FragmentManager, account: Account) {
//        var exercisesFragment = fragmentManager.findFragmentByTag(ReposFragment::class.java.name)
//        if (exercisesFragment == null)
        val exercisesFragment = ReposFragment.forAccount(account)

        val transaction = fragmentManager.beginTransaction()
        // Store the Fragment in stack
        transaction.addToBackStack(ReposFragment::class.java.name)
        //        transaction.setCustomAnimations(R.anim.enter_from_center,R.anim.exit_from_center);
        transaction.replace(R.id.container, exercisesFragment,
                ReposFragment::class.java.name).commit()
    }

    fun navigateToUploadsView(context: Context, fragmentManager: FragmentManager) {
        var exercisesFragment = fragmentManager.findFragmentByTag(UploadsFragment::class.java.name)
        if (exercisesFragment == null)
            exercisesFragment = UploadsFragment()

        val transaction = fragmentManager.beginTransaction()
        // Store the Fragment in stack
        transaction.addToBackStack(UploadsFragment::class.java.name)
        //        transaction.setCustomAnimations(R.anim.enter_from_center,R.anim.exit_from_center);
        transaction.replace(R.id.container, exercisesFragment,
                UploadsFragment::class.java.name).commit()
    }

    fun navigateToAddAccountView(context: Context, fragmentManager: FragmentManager) {

        //        var exercisesFragment = fragmentManager.findFragmentByTag(ReposFragment::class.java.name)
//        if (exercisesFragment == null)
        val addAccountFragment = AddAccountFragment.forEmptyAccount()

        val transaction = fragmentManager.beginTransaction()
        // Store the Fragment in stack
        transaction.addToBackStack(AddAccountFragment::class.java.name)
        //        transaction.setCustomAnimations(R.anim.enter_from_center,R.anim.exit_from_center);
        transaction.replace(R.id.container, addAccountFragment, AddAccountFragment::
        class.java.name).commit()
    }
}
