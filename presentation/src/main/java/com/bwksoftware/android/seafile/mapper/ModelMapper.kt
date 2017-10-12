package com.bwksoftware.android.seafile.mapper

import com.bwksoftware.android.seafile.domain.AvatarTemplate
import com.bwksoftware.android.seafile.domain.ItemTemplate
import com.bwksoftware.android.seafile.model.Avatar
import com.bwksoftware.android.seafile.model.DirectoryItem
import com.bwksoftware.android.seafile.model.FileItem
import com.bwksoftware.android.seafile.model.Item
import javax.inject.Inject


class ModelMapper @Inject constructor() {
    fun transformAvatar(avatar: AvatarTemplate): Avatar {
        return Avatar(avatar.url)
    }

    fun transformItem(item: ItemTemplate): Item {
        return when(item.type){
            "file" -> FileItem(item.id, item.name, item.size)
            "dir" -> DirectoryItem(item.id, item.name, item.size)
            else -> Item(item.id, item.name, item.size,Item.UNKNOWN)
        }
    }

    fun transformItemList(itemList: List<ItemTemplate>): List<Item> {
        return itemList.mapTo(ArrayList<Item>()) { transformItem(it) }
    }

}