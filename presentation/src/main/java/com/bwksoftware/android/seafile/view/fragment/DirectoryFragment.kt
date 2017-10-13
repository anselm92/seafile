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

package com.bwksoftware.android.seafile.view.fragment

import android.accounts.Account
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.authentication.SeafAccountManager
import com.bwksoftware.android.seafile.model.Item
import com.bwksoftware.android.seafile.presenter.DirectoryPresenter
import com.bwksoftware.android.seafile.view.adapter.DirectoryAdapter
import com.bwksoftware.android.seafile.view.views.DirectoryView
import javax.inject.Inject


class DirectoryFragment : BaseFragment(), DirectoryView, DirectoryAdapter.OnItemClickListener {


    interface OnDirectoryClickedListener {
        fun onDirectoryClicked(fragment: BaseFragment, repoId: String, repoName: String,
                               directory: String)

        fun onFileClicked(fragment: BaseFragment, repoId: String, repoName: String,
                          directory: String, file: String)
    }

    companion object {
        private const val PARAM_ACCOUNT = "param_account"
        private const val PARAM_DIRECTORY = "param_directory"
        private const val PARAM_REPOID = "param_repoid"
        private const val PARAM_REPONAME = "param_reponame"

        fun forAccountRepoAndDir(account: Account, repoId: String,
                                 repoName: String,
                                 directory: String): DirectoryFragment {
            val reposFragment = DirectoryFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_ACCOUNT, account.name)
            arguments.putString(PARAM_DIRECTORY, directory)
            arguments.putString(PARAM_REPOID, repoId)
            arguments.putString(PARAM_REPONAME, repoName)
            reposFragment.arguments = arguments
            return reposFragment
        }
    }

    @Inject lateinit var directoryPresenter: DirectoryPresenter
    @Inject lateinit var seafAccountManager: SeafAccountManager

    lateinit var directoryAdapter: DirectoryAdapter

    lateinit var rvDirectory: RecyclerView

    override fun layoutId() = R.layout.fragment_directory

    override fun name() = arguments.getString(PARAM_REPONAME) + arguments.getString(
            PARAM_DIRECTORY)

    override fun activity() = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        val address = seafAccountManager.getServerAddress(seafAccountManager.getCurrentAccount())
        directoryAdapter = DirectoryAdapter(this,
                address!!,
                arguments.getString(PARAM_REPOID),
                arguments.getString(
                        PARAM_DIRECTORY), seafAccountManager.getCurrentAccountToken(), context)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvDirectory = view?.findViewById(R.id.rv_directory)!!
        rvDirectory.adapter = directoryAdapter
        rvDirectory.layoutManager = LinearLayoutManager(this.context)
        val mDividerItemDecoration = DividerItemDecoration(
                rvDirectory.getContext(),
                (rvDirectory.layoutManager as LinearLayoutManager).orientation
        )

        rvDirectory.addItemDecoration(mDividerItemDecoration)
        if (firstTimeCreated(savedInstanceState)) {
            initializeView()
            loadDirectory()
        }
    }

    override fun renderDirectoryEntries(entries: List<Item>) {
        directoryAdapter.setItems(entries)
        directoryAdapter.notifyDataSetChanged()
    }

    override fun onDirectoryClicked(item: Item) {
        val attachedActivity = activity
        when (attachedActivity) {
            is OnDirectoryClickedListener -> attachedActivity.onDirectoryClicked(this,
                    arguments.getString(PARAM_REPOID),
                    arguments.getString(PARAM_REPONAME),
                    arguments.getString(PARAM_DIRECTORY) + "/" + item.name)
        }
    }

    override fun onFileClicked(item: Item) {
        val attachedActivity = activity
        when (attachedActivity) {
            is OnDirectoryClickedListener -> attachedActivity.onFileClicked(this,
                    arguments.getString(PARAM_REPOID),
                    arguments.getString(PARAM_REPONAME),
                    arguments.getString(PARAM_DIRECTORY), item.name!!)
        }
    }

    private fun loadDirectory() {
        directoryPresenter.getDirectoryEntries(arguments.getString(PARAM_ACCOUNT),
                arguments.getString(
                        PARAM_REPOID), arguments.getString(PARAM_DIRECTORY))
    }

    private fun initializeView() {
        directoryPresenter.directoryView = this
        rvDirectory.setHasFixedSize(true)
        rvDirectory.setItemViewCacheSize(20)
        rvDirectory.isDrawingCacheEnabled = true
        rvDirectory.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    }

}