package com.bwksoftware.android.seafile.model

/**
 * Created by anselm.binninger on 12/10/2017.
 */
class DirectoryItem(id: String?, name: String?, size: Long) : Item(id, name,
        size, TYPE_DIRECTORY) {

}