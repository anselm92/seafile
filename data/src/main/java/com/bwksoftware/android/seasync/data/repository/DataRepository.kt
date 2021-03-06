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

package com.bwksoftware.android.seasync.data.repository

import com.bwksoftware.android.seasync.data.entity.entityDataMapper
import com.bwksoftware.android.seasync.data.net.RestApiImpl
import com.bwksoftware.android.seasync.domain.AccountTemplate
import com.bwksoftware.android.seasync.domain.AvatarTemplate
import com.bwksoftware.android.seasync.domain.ItemTemplate
import com.bwksoftware.android.seasync.domain.RepoTemplate
import com.bwksoftware.android.seasync.domain.repository.Repository
import io.reactivex.Observable
import javax.inject.Inject


class DataRepository @Inject constructor(private val restService: RestApiImpl) : Repository {
    override fun getDirectoryEntries(authToken: String, repoId: String,
                                     directory: String): Observable<List<ItemTemplate>> {
        return restService.getDirectoryEntries(authToken, repoId, directory).map(
                entityDataMapper::transformItemList)
    }

    override fun getAvatar(username: String, token: String): Observable<AvatarTemplate> {
        return restService.getAvatar(username, token).map(entityDataMapper::transformAvatar)
    }

    override fun getRepoList(authToken: String): Observable<List<RepoTemplate>> {
        return restService.getRepoList(authToken).map(entityDataMapper::transformRepoList)
    }

    override fun getAccountToken(username: String, password: String): Observable<AccountTemplate> {
        return restService.getAccountToken(username, password).map(
                entityDataMapper::transformAccountToken)
    }
}