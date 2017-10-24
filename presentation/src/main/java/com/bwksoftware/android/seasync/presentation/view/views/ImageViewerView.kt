package com.bwksoftware.android.seasync.presentation.view.views

import com.bwksoftware.android.seasync.presentation.model.Item

/**
 * Created by anselm.binninger on 12/10/2017.
 */
interface ImageViewerView : LoadDataView {
    fun renderImages(images: List<Item>)
}