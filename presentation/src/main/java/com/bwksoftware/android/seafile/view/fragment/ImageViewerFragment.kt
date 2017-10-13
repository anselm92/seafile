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
import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.RelativeLayout
import android.widget.TextView
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.authentication.SeafAccountManager
import com.bwksoftware.android.seafile.model.Item
import com.bwksoftware.android.seafile.presenter.ImageViewerPresenter
import com.bwksoftware.android.seafile.utils.FileUtils
import com.bwksoftware.android.seafile.view.activity.AccountActivity
import com.bwksoftware.android.seafile.view.adapter.ImageViewerAdapter
import com.bwksoftware.android.seafile.view.views.ImageViewerView
import javax.inject.Inject






class ImageViewerFragment : BaseFragment(), ImageViewerView {


    companion object {
        private const val PARAM_ACCOUNT = "param_account"
        private const val PARAM_DIRECTORY = "param_directory"
        private const val PARAM_REPOID = "param_repoid"
        private const val PARAM_FILE = "param_file"
        private const val PARAM_REPONAME = "param_reponame"

        fun forAccountRepoAndDir(account: Account, repoId: String,
                                 repoName: String,
                                 directory: String,
                                 file: String): ImageViewerFragment {
            val reposFragment = ImageViewerFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_ACCOUNT, account.name)
            arguments.putString(PARAM_REPOID, repoId)
            arguments.putString(PARAM_FILE, file)
            arguments.putString(PARAM_DIRECTORY, directory)

            arguments.putString(PARAM_REPONAME, repoName)
            reposFragment.arguments = arguments
            return reposFragment
        }
    }

    @Inject lateinit var seafAccountManager: SeafAccountManager

    @Inject lateinit var imageViewerPresenter: ImageViewerPresenter

    lateinit var imageViewerPagerAdapter: ImageViewerAdapter

    lateinit var mPager: ViewPager

    lateinit var currentPage: TextView

    lateinit var bottomToolBar: RelativeLayout

    override fun layoutId() = R.layout.fragment_imageviewer

    override fun activity(): Activity = activity

    override fun name() = arguments.getString(PARAM_REPONAME) + arguments.getString(
            PARAM_DIRECTORY)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        val address = seafAccountManager.getServerAddress(seafAccountManager.getCurrentAccount())
        imageViewerPagerAdapter = ImageViewerAdapter(context,
                address!!,
                arguments.getString(PARAM_DIRECTORY),
                arguments.getString(PARAM_REPOID), seafAccountManager.getCurrentAccountToken())
    }

    private fun loadImages() {
        imageViewerPresenter.getImages(arguments.getString(ImageViewerFragment.PARAM_ACCOUNT),
                arguments.getString(
                        ImageViewerFragment.PARAM_REPOID), arguments.getString(
                ImageViewerFragment.PARAM_DIRECTORY))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPager = view!!.findViewById(R.id.pager)
        currentPage = view.findViewById(R.id.current_page)
        bottomToolBar = view.findViewById(R.id.viewpager_bottom_toolbar)
        mPager.adapter = imageViewerPagerAdapter
        if (firstTimeCreated(savedInstanceState)) {
            initializeView()
            loadImages()
        }
        setFullscreen(activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        exitFullscreen(activity)
    }

    fun initializeView() {
        imageViewerPresenter.imageViewerView = this

        mPager.setOnTouchListener(OnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP) {
                view?.performClick()
                true
            }
            false
        })

        mPager.setOnClickListener({
            when (bottomToolBar.visibility) {
                View.VISIBLE -> bottomToolBar.visibility = View.GONE
                View.GONE -> bottomToolBar.visibility = View.VISIBLE
            }
        })
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setCurrentPageText(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun setCurrentPageText(index: Int) {
        val allPages = imageViewerPagerAdapter.count
        var currPage = index + 1
        val text = "$currPage / $allPages"
        currentPage.text = text
    }

    override fun renderImages(entries: List<Item>) {
        val chosenFile = arguments.getString(PARAM_FILE)

        val images = entries.filter { FileUtils.isViewableImage(it.name!!) }
        val index = (0 until images.size).firstOrNull { images[it].name == chosenFile }
                ?: 0
        imageViewerPagerAdapter.setItems(images)
        setCurrentPageText(index)
        imageViewerPagerAdapter.notifyDataSetChanged()
        mPager.setCurrentItem(index, false)
    }


    fun setFullscreen(activity: Activity) {
        if (activity is AccountActivity) {
            activity.toolbar.visibility = View.GONE
            activity.coordinator.fitsSystemWindows = false
        }
        activity.window.decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    fun exitFullscreen(activity: Activity) {
        if (activity is AccountActivity) {
            activity.toolbar.visibility = View.VISIBLE
            activity.coordinator.fitsSystemWindows = true
        }
        activity.window.decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}