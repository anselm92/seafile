package com.bwksoftware.android.seasync.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by anselm.binninger on 12/10/2017.
 */
class Avatar {
    @SerializedName("url")
    @Expose
    open var url: String? = null
}