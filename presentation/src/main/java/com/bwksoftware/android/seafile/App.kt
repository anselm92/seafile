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

package com.bwksoftware.android.seafile

import android.app.Application
import android.content.Context
import com.bwksoftware.android.seafile.components.AuthImageDownloader
import com.bwksoftware.android.seafile.internal.di.components.ApplicationComponent
import com.bwksoftware.android.seafile.internal.di.components.DaggerApplicationComponent
import com.bwksoftware.android.seafile.internal.di.modules.ApplicationModule
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType


class App : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        initImageLoader(applicationContext);
    }

    fun initImageLoader(context: Context) {

//        val cacheDir = StorageManager.getInstance().getThumbnailsDir()
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        val config = ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(10)
                .diskCacheFileNameGenerator(Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDownloader(AuthImageDownloader(context, 10000, 10000))
                .writeDebugLogs() // Remove for release app
                .build()
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config)
    }

}