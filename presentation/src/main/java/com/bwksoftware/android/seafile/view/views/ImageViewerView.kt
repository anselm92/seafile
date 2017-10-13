package com.bwksoftware.android.seafile.view.views

import com.bwksoftware.android.seafile.model.Item

/**
 * Created by anselm.binninger on 12/10/2017.
 */
interface ImageViewerView : LoadDataView{
    fun renderImages(images: List<Item>)
}