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

package com.bwksoftware.android.seasync.presentation.mapper

import com.bwksoftware.android.seasync.domain.RepoTemplate
import com.bwksoftware.android.seasync.presentation.R
import com.bwksoftware.android.seasync.presentation.model.Repo
import javax.inject.Inject
import android.accounts.Account as AndroidAccount
import com.bwksoftware.android.seasync.presentation.model.Account as SeafAccount

/**
 * Created by ansel on 10/11/2017.
 */
class RepoModelMapper @Inject constructor() {

    fun transformRepo(repo: RepoTemplate): Repo {
        var drawable: Int? = null
        when (repo.permission) {
            "rw" -> {
                drawable = R.drawable.repo
            }
            "r" -> {
                drawable = R.drawable.repo_readonly
            }
        }
        if (repo.encrypted!!)
            drawable = R.drawable.repo_encrypted
        return Repo(repo.id, repo.name, repo.permission, repo.owner, repo.encrypted, repo.mtime,
                repo.size,
                drawable)
    }

    fun transformRepos(repos: List<RepoTemplate>): List<Repo> {
        return repos.mapTo(ArrayList()) { transformRepo(it) }
    }

}