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
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bwksoftware.android.seasync.presentation.R
import com.bwksoftware.android.seasync.presentation.model.Account
import com.bwksoftware.android.seasync.presentation.model.NavBaseItem
import com.bwksoftware.android.seasync.presentation.model.NavBaseItem.Companion.TYPE_ACCOUNT
import com.bwksoftware.android.seasync.presentation.model.NavBaseItem.Companion.TYPE_BUTTON
import com.bwksoftware.android.seasync.presentation.model.NavBaseItem.Companion.TYPE_HEADER
import com.bwksoftware.android.seasync.presentation.model.NavButton
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

class AccountAdapter(val onItemClickLister: OnItemClickListener,
                     val context: Context) : Adapter<RecyclerView.ViewHolder>() {

    private val mItems: ArrayList<NavBaseItem> = ArrayList()
    val downloader = OkHttp3Downloader(context)

    fun setItems(newItems: List<NavBaseItem>) {
        mItems.clear()
        mItems.addAll(newItems)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) return TYPE_HEADER else mItems[position].itemType
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_HEADER -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.nav_menu_header,
                        parent, false);
                HeaderHolder(view)
            }
            TYPE_ACCOUNT -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.nav_menu_accounts_item,
                        parent, false);
                AccountHolder(view)
            }
            TYPE_BUTTON -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.nav_menu_button_item,
                        parent, false);
                ButtonHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.nav_menu_accounts_item,
                        parent, false);
                AccountHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val item = mItems[position]
        if (holder is HeaderHolder && item is Account) {
            Picasso.Builder(context).downloader(downloader).build().load(item.imgLink).into(
                    holder.accountImg)
            holder.accountName.text = item.name
        } else if (holder is AccountHolder && item is Account) {
            Picasso.Builder(context).downloader(downloader).build().load(item.imgLink).into(
                    holder.accountImg)
            holder.accountName.text = item.name
        } else if (holder is ButtonHolder && item is NavButton) {
            holder.itemImg.setImageDrawable(item.imgDrawable)
            holder.itemName.text = item.name
        }
    }

    inner class ButtonHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView), View.OnClickListener {
        val itemImg: ImageView = itemView.findViewById(R.id.item_img)
        val itemName: TextView = itemView.findViewById(R.id.item_name)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val item: NavBaseItem = mItems[layoutPosition]
            if (item is NavButton) {
                onItemClickLister.onButtonClicked(item.resourceId)
            }

        }

    }

    inner class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accountImg: ImageView = itemView.findViewById(R.id.account_item_img)
        val accountName: TextView = itemView.findViewById(R.id.account_item_name)
        val selectButton: TextView = itemView.findViewById(R.id.header_button)

        init {
            selectButton.setOnClickListener({ onItemClickLister.onHeaderButtonClicked() })
        }
    }

    inner class AccountHolder(itemView: View) : RecyclerView.ViewHolder(
            itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val item: NavBaseItem = mItems[layoutPosition]
            if (item is Account) {
                onItemClickLister.onAccountClicked(item)
            }

        }

        val accountImg: ImageView = itemView.findViewById(R.id.account_item_img)
        val accountName: TextView = itemView.findViewById(R.id.account_item_name)
    }

    interface OnItemClickListener {
        fun onAccountClicked(account: Account)
        fun onButtonClicked(itemId: Int)
        fun onHeaderButtonClicked()
    }


}