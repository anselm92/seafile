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

package com.bwksoftware.android.seasync.presentation.view.adapter

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
import com.bwksoftware.android.seasync.presentation.R
import com.bwksoftware.android.seasync.presentation.model.Repo
import com.bwksoftware.android.seasync.presentation.utils.FileUtils


class RepoAdapter(val onItemClickLister: OnItemClickListener,
                  val context: Context) : Adapter<RepoAdapter.RepoHolder>() {

    private val mItems: ArrayList<Repo> = ArrayList()

    fun setItems(newItems: List<Repo>) {
        mItems.clear()
        mItems.addAll(newItems)
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RepoHolder {

        val view = LayoutInflater.from(parent?.context).inflate(R.layout.repo_item,
                parent, false)
        return RepoHolder(view)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        val item = mItems[position]

        holder.repoImg.setImageDrawable(context.getDrawable(item.drawable!!))
        holder.repoName.text = item.name
        holder.repoDateModified.text = FileUtils.translateCommitTime(item.mtime!! * 1000, context)

    }

    inner class RepoHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val item: Repo = mItems[layoutPosition]
            onItemClickLister.onRepoClicked(item)


        }

        val repoImg: ImageView = itemView.findViewById(R.id.repo_img)
        val repoName: TextView = itemView.findViewById(R.id.repo_name)
        val repoDateModified: TextView = itemView.findViewById(R.id.repo_datemodified)

    }

    interface OnItemClickListener {
        fun onRepoClicked(repo: Repo)
    }


}