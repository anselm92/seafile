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

package com.bwksoftware.android.seafile.domain.interactor

import com.bwksoftware.android.seafile.domain.AccountTemplate
import com.bwksoftware.android.seafile.domain.RepoTemplate
import com.bwksoftware.android.seafile.domain.executor.PostExecutionThread
import com.bwksoftware.android.seafile.domain.executor.ThreadExecutor
import com.bwksoftware.android.seafile.domain.repository.Repository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a collection of all [WorkoutTemplate].
 */
class GetAccountToken @Inject
internal constructor(val repository: Repository, threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread) : UseCase<AccountTemplate, GetAccountToken.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Observable<AccountTemplate> {
        return repository.getAccountToken(params.username,params.password)
    }

    class Params(val username: String, val password: String)

}