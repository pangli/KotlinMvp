package com.zorro.kotlin.samples.popup

import android.content.Context
import android.graphics.Color
import android.support.annotation.NonNull
import com.lxj.xpopup.core.ImageViewerPopupView

/**
 * Created by Zorro on 2019/11/22.
 * 备注：查看图片PopupView
 */
class ImageViewPopup(@NonNull context: Context) : ImageViewerPopupView(context) {
    override fun initPopupContent() {
        super.initPopupContent()
        bgColor = Color.parseColor("#ba000000") //弹窗的背景颜色
    }
}