package com.bwksoftware.android.seasync.presentation.components

import android.support.v4.app.Fragment
import com.bwksoftware.android.seasync.presentation.view.activity.AccountActivity
import com.bwksoftware.android.seasync.presentation.view.fragment.BaseFragment


class BackPressImpl(private val parentFragment: Fragment?) : OnBackPressListener {

    override fun onBackPressed(): Boolean {

        if (parentFragment == null) return false

        val childCount = parentFragment!!.childFragmentManager.backStackEntryCount

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false

        } else {
            // get the child Fragment
            val childFragmentManager = parentFragment.childFragmentManager
            val lastFragmentName = childFragmentManager.getBackStackEntryAt(
                    childFragmentManager.backStackEntryCount - 1).name
            val childFragment = childFragmentManager.findFragmentByTag(
                    lastFragmentName) as OnBackPressListener
            // propagate onBackPressed method call to the child Fragment
            if (!childFragment.onBackPressed()) {
                // child Fragment was unable to handle the task
                // It could happen when the child Fragment is last last leaf of a chain
                // removing the child Fragment from stack
                childFragmentManager.popBackStackImmediate()
                parentFragment.onResume()
                (parentFragment.activity as? AccountActivity)?.setTitle(
                        (parentFragment as BaseFragment).name())

            }

            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true
        }
    }
}