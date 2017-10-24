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

import android.content.Context
import com.bwksoftware.android.seasync.domain.executor.PostExecutionThread
import com.bwksoftware.android.seasync.domain.executor.ThreadExecutor
import com.bwksoftware.android.seasync.domain.repository.Repository
import com.bwksoftware.android.seasync.presentation.App
import com.bwksoftware.android.seasync.presentation.internal.di.modules.ApplicationModule
import com.bwksoftware.android.seasync.presentation.view.fragment.*
import dagger.Component
import javax.inject.Singleton

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unsco-ped bindings.
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(androidApplication: App)

    fun inject(reposFragment: ReposFragment)
    fun inject(uploadsFragment: UploadsFragment)
    fun inject(addAccountFragment: AddAccountFragment)
    fun inject(directoryFragment: DirectoryFragment)
    fun inject(imageViewerFragment: ImageViewerFragment)

    //    val androidApplication: App
//
    fun context(): Context

    fun threadExecutor(): ThreadExecutor

    fun postExecutionThread(): PostExecutionThread

    fun dataRepository(): Repository
}
