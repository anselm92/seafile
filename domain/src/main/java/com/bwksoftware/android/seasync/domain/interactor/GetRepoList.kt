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
package com.bwksoftware.android.seasync.domain.interactor

import javax.inject.Inject

import io.reactivex.Observable
import com.bwksoftware.android.seasync.domain.RepoTemplate
import com.bwksoftware.android.seasync.domain.executor.PostExecutionThread
import com.bwksoftware.android.seasync.domain.executor.ThreadExecutor
import com.bwksoftware.android.seasync.domain.repository.Repository

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a collection of all [WorkoutTemplate].
 */
class GetRepoList  @Inject
internal constructor(val repository: Repository, threadExecutor: ThreadExecutor,
                     postExecutionThread: PostExecutionThread) : UseCase<List<RepoTemplate>, GetRepoList.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Params): Observable<List<RepoTemplate>> {
        return repository.getRepoList(params.authToken)
    }

    class Params(private val forceUpdate: Boolean, val authToken: String)

}
