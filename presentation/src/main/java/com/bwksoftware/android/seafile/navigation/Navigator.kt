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
import android.support.v4.app.Fragment
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.view.fragment.ReposFragment
import com.bwksoftware.android.seafile.view.fragment.UploadsFragment
import javax.inject.Inject

class Navigator @Inject constructor() {
    fun navigateToReposView(context: Context, callingFragment: Fragment, account: Account) {
        var exercisesFragment = callingFragment.childFragmentManager.findFragmentByTag(ReposFragment::class.java.name)
        if (exercisesFragment == null)
            exercisesFragment = ReposFragment.forAccount(account)

        val transaction = callingFragment.childFragmentManager.beginTransaction()
        // Store the Fragment in stack
        transaction.addToBackStack(ReposFragment::class.java.name)
        //        transaction.setCustomAnimations(R.anim.enter_from_center,R.anim.exit_from_center);
        transaction.replace(R.id.container, exercisesFragment, ReposFragment::class.java.name).commit()
    }

    fun navigateToUploadsView(context: Context, callingFragment: Fragment) {
        var exercisesFragment = callingFragment.childFragmentManager.findFragmentByTag(UploadsFragment::class.java.name)
        if (exercisesFragment == null)
            exercisesFragment = UploadsFragment()

        val transaction = callingFragment.childFragmentManager.beginTransaction()
        // Store the Fragment in stack
        transaction.addToBackStack(UploadsFragment::class.java.name)
        //        transaction.setCustomAnimations(R.anim.enter_from_center,R.anim.exit_from_center);
        transaction.replace(R.id.container, exercisesFragment, UploadsFragment::class.java.name).commit()
    }
}
