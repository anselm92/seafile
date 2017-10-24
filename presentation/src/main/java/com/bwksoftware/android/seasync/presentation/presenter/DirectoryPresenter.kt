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

package com.bwksoftware.android.seasync.presentation.presenter

import android.util.Log
import com.bwksoftware.android.seasync.domain.ItemTemplate
import com.bwksoftware.android.seasync.domain.interactor.DefaultObserver
import com.bwksoftware.android.seasync.domain.interactor.GetDirectoryEntries
import com.bwksoftware.android.seasync.presentation.authentication.Authenticator
import com.bwksoftware.android.seasync.presentation.mapper.ModelMapper
import com.bwksoftware.android.seasync.presentation.view.views.DirectoryView
import javax.inject.Inject


class DirectoryPresenter @Inject constructor(val getDirectoryEntries: GetDirectoryEntries,
                                             val modelMapper: ModelMapper) {

    internal lateinit var directoryView: DirectoryView
    @Inject lateinit var authenticator: Authenticator


    fun getDirectoryEntries(accountName: String, repoId: String, directory: String) {

        val authToken = authenticator.getCurrentUserAuthToken(accountName, directoryView.activity())
        this.getDirectoryEntries.execute(
                DirectoryObserver(), GetDirectoryEntries.Params(authToken, repoId, directory))
    }


    private inner class DirectoryObserver : DefaultObserver<List<ItemTemplate>>() {

        override fun onComplete() {
            Log.d("AccountPresenter", "yolo complete")
        }

        override fun onError(exception: Throwable) {
            Log.d("AccountPresenter", "yolo error" + exception.localizedMessage)
        }

        override fun onNext(directoryEntries: List<ItemTemplate>) {
            directoryView.renderDirectoryEntries(modelMapper.transformItemList(directoryEntries))
        }
    }
}