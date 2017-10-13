package com.bwksoftware.android.seafile.view.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.model.Item
import com.bwksoftware.android.seafile.utils.FileUtils
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import java.net.URLEncoder




class ImageViewerAdapter(val context: Context,
                         val address: String,
                         val directory: String,
                         val repoId: String,
                         val token: String) : PagerAdapter() {

    private val mItems: ArrayList<Item> = ArrayList()

    fun setItems(newItems: List<Item>) {
        mItems.clear()
        mItems.addAll(newItems)
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val item = mItems[position]
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = inflater.inflate(R.layout.fragment_imageviewerpage, container,
                false)
        val imgDisplay: ImageView = viewLayout.findViewById(R.id.imgDisplay)
        val btnClose: Button = viewLayout.findViewById(R.id.btnClose)

        val fileName = URLEncoder.encode(directory+ "/"+item.name, "UTF-8")
        val url = FileUtils.getThumbnailUrl(address, repoId, fileName, 600)
        ImageLoader.getInstance().displayImage(url, imgDisplay, getDisplayImageOptions);

        (container as ViewPager).addView(viewLayout)
        return viewLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    var getDisplayImageOptions: DisplayImageOptions? =
            DisplayImageOptions.Builder()
                    .extraForDownloader(token)
                    .delayBeforeLoading(0)
                    .resetViewBeforeLoading(true)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build()
}