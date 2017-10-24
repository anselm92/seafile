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

package com.bwksoftware.android.seasync.presentation.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bwksoftware.android.seasync.presentation.App
import com.bwksoftware.android.seasync.presentation.components.BackPressImpl
import com.bwksoftware.android.seasync.presentation.components.OnBackPressListener
import com.bwksoftware.android.seasync.presentation.internal.di.components.ApplicationComponent


abstract class BaseFragment : Fragment(), OnBackPressListener {
    init {
        retainInstance = true
    }

    abstract fun layoutId(): Int

    abstract fun name(): String

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity.application as App).component
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?) =
            inflater?.inflate(layoutId(), container, false)

    override fun onBackPressed(): Boolean {
        return BackPressImpl(this).onBackPressed()
    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null
}