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

package com.bwksoftware.android.seafile.presenter

import android.util.Log
import com.bwksoftware.android.seafile.authentication.Authenticator
import com.bwksoftware.android.seafile.domain.ItemTemplate
import com.bwksoftware.android.seafile.domain.interactor.DefaultObserver
import com.bwksoftware.android.seafile.domain.interactor.GetDirectoryEntries
import com.bwksoftware.android.seafile.mapper.ModelMapper
import com.bwksoftware.android.seafile.view.views.ImageViewerView
import javax.inject.Inject


class ImageViewerPresenter @Inject constructor(val getDirectoryEntries: GetDirectoryEntries, val modelMapper: ModelMapper) {

    internal lateinit var imageViewerView: ImageViewerView
    @Inject lateinit var authenticator:Authenticator


    fun getImages(accountName : String, repoId: String, directory: String) {

        val authToken = authenticator.getCurrentUserAuthToken(accountName, imageViewerView.activity())
        this.getDirectoryEntries.execute(
                FileObserver(), GetDirectoryEntries.Params(authToken, repoId, directory))
    }


    private inner class FileObserver : DefaultObserver<List<ItemTemplate>>() {

        override fun onComplete() {
            Log.d("AccountPresenter", "yolo complete")
        }

        override fun onError(exception: Throwable) {
            Log.d("AccountPresenter", "yolo error" + exception.localizedMessage)
        }

        override fun onNext(images: List<ItemTemplate>) {
            imageViewerView.renderImages(modelMapper.transformItemList(images))
        }
    }
}