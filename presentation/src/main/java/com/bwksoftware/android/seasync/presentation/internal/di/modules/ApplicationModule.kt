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
package com.bwksoftware.android.seasync.presentation.internal.di.modules

import android.content.Context
import com.bwksoftware.android.seasync.data.executor.JobExecutor
import com.bwksoftware.android.seasync.data.repository.DataRepository
import com.bwksoftware.android.seasync.domain.executor.PostExecutionThread
import com.bwksoftware.android.seasync.domain.executor.ThreadExecutor
import com.bwksoftware.android.seasync.domain.repository.Repository
import com.bwksoftware.android.seasync.presentation.App
import com.bwksoftware.android.seasync.presentation.UIThread
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
class ApplicationModule(private val androidApplication: App) {

    @Provides
    @Singleton
    fun provideApp(): App {
        return androidApplication
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return androidApplication
    }


    @Provides
    @Singleton
    fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor {
        return jobExecutor
    }

    @Provides
    @Singleton
    fun providePostExecutionThread(uiThread: UIThread): PostExecutionThread {
        return uiThread
    }

//    @Provides
//    @Singleton
//    fun provideSharedPreferences(): SharedPreferences {
//        return androidApplication.getSharedPreferences("app", Context.MODE_PRIVATE)
//    }

    @Provides
    @Singleton
    fun provideDataRepository(dataRepository: DataRepository): Repository {
        return dataRepository

    }
}
