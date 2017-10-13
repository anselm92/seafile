package com.bwksoftware.android.seafile.model


open class Item(val id: String?, val name: String?, val mtime : Long?,val size: Long, val type: Int){
    companion object {
        val TYPE_FILE = 0
        val TYPE_DIRECTORY = 1
        val UNKNOWN = -1
    }
}