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
package com.bwksoftware.android.seasync.presentation.internal.di.components

import com.bwksoftware.android.seasync.presentation.internal.di.modules.ActivityModule
import com.bwksoftware.android.seasync.presentation.internal.di.scope.PerActivity
import com.bwksoftware.android.seasync.presentation.view.activity.AccountActivity
import dagger.Component

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 *
 * Subtypes of ActivityComponent should be decorated with annotation:
 * [PerActivity]
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(accountActivity: AccountActivity)
}
