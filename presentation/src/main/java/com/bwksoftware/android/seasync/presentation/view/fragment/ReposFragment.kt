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

package com.bwksoftware.android.seasync.presentation.view.fragment

import android.accounts.Account
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bwksoftware.android.seasync.presentation.R
import com.bwksoftware.android.seasync.presentation.model.Repo
import com.bwksoftware.android.seasync.presentation.presenter.RepoPresenter
import com.bwksoftware.android.seasync.presentation.view.adapter.RepoAdapter
import com.bwksoftware.android.seasync.presentation.view.views.RepoView
import javax.inject.Inject


class ReposFragment : BaseFragment(), RepoView, RepoAdapter.OnItemClickListener {

    interface OnRepoClickedListener {
        fun onRepoClicked(fragment: BaseFragment, repoId: String, repoName: String)
    }

    companion object {
        private const val PARAM_ACCOUNT = "param_account"

        fun forAccount(account: Account): ReposFragment {
            val reposFragment = ReposFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_ACCOUNT, account.name)
            reposFragment.arguments = arguments
            return reposFragment
        }
    }

    @Inject lateinit var repoPresenter: RepoPresenter
    lateinit var repoAdapter: RepoAdapter

    lateinit var rvRepos: RecyclerView

    override fun layoutId() = R.layout.fragment_repos

    override fun name() = "Repos"

    override fun activity() = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        repoAdapter = RepoAdapter(this, context)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvRepos = view?.findViewById(R.id.rv_repos)!!
        rvRepos.adapter = repoAdapter
        rvRepos.layoutManager = LinearLayoutManager(this.context)
        val mDividerItemDecoration = DividerItemDecoration(
                rvRepos.getContext(),
                (rvRepos.layoutManager as LinearLayoutManager).orientation
        )
        rvRepos.addItemDecoration(mDividerItemDecoration)
        if (firstTimeCreated(savedInstanceState)) {
            initializeView()
            loadRepos()
        }
    }

    override fun renderRepos(repos: List<Repo>) {
        repoAdapter.setItems(repos)
        repoAdapter.notifyDataSetChanged()
    }

    override fun onRepoClicked(repo: Repo) {
        val attachedActivity = activity
        when (attachedActivity) {
            is OnRepoClickedListener -> attachedActivity.onRepoClicked(this,
                    repo.id!!, repo.name!!)
        }
    }

    private fun loadRepos() {
        repoPresenter.getRepos(arguments.getString(PARAM_ACCOUNT))
    }

    private fun initializeView() {
        repoPresenter.repoView = this

    }

}