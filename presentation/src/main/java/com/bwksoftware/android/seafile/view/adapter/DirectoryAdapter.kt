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

package com.bwksoftware.android.seafile.view.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bwksoftware.android.seafile.R
import com.bwksoftware.android.seafile.model.DirectoryItem
import com.bwksoftware.android.seafile.model.FileItem
import com.bwksoftware.android.seafile.model.Item
import com.bwksoftware.android.seafile.model.Item.Companion.TYPE_DIRECTORY
import com.bwksoftware.android.seafile.model.Item.Companion.TYPE_FILE
import com.bwksoftware.android.seafile.utils.FileUtils
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import java.net.URLEncoder


class DirectoryAdapter(val onItemClickLister: OnItemClickListener,
                       val address: String,
                       val repoId: String, val directory: String,
                       val token: String,
                       val context: Context) : Adapter<RecyclerView.ViewHolder>() {

    private val mItems: ArrayList<Item> = ArrayList()

    fun setItems(newItems: List<Item>) {
        mItems.clear()
        mItems.addAll(newItems)
    }

    override fun getItemViewType(position: Int): Int {
        return mItems[position].type
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_FILE -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.file_item,
                        parent, false)
                FileHolder(view)
            }
            TYPE_DIRECTORY -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.directory_item,
                        parent, false)
                DirectoryHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.directory_item,
                        parent, false)
                DirectoryHolder(view)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mItems[position]

        if (holder is FileHolder && item is FileItem) {
            holder.itemName.text = item.name
            if (FileUtils.isViewableImage(item.name!!)) {
                val file = URLEncoder.encode(directory + "/" + item.name, "UTF-8")
                val url = FileUtils.getThumbnailUrl(address, repoId, file, 100)
                ImageLoader.getInstance().displayImage(url, holder.itemImg, getDisplayImageOptions);

            } else
                holder.itemImg.setImageResource(R.drawable.empty_profile)

        } else if (holder is DirectoryHolder && item is DirectoryItem) {
            holder.itemImg.setImageResource(R.drawable.folder)
            holder.itemName.text = item.name
        }

        //holder.repoImg.setImageDrawable(context.getDrawable(item.drawable!!))

    }

    inner class FileHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val item: Item = mItems[layoutPosition]
            onItemClickLister.onFileClicked(item)
        }

        val itemImg: ImageView = itemView.findViewById(R.id.file_img)
        val itemName: TextView = itemView.findViewById(R.id.file_name)
    }

    inner class DirectoryHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val item: Item = mItems[layoutPosition]
            onItemClickLister.onDirectoryClicked(item)
        }

        val itemImg: ImageView = itemView.findViewById(R.id.directory_img)
        val itemName: TextView = itemView.findViewById(R.id.directory_name)
    }

    interface OnItemClickListener {
        fun onDirectoryClicked(item: Item)
        fun onFileClicked(item: Item)
    }

    var getDisplayImageOptions: DisplayImageOptions? =
        DisplayImageOptions.Builder()
                .extraForDownloader(token)
                .delayBeforeLoading(500)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.empty_profile)
                .showImageOnFail(R.drawable.empty_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build()


}