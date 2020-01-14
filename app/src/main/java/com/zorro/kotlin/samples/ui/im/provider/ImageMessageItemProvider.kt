package com.zorro.kotlin.samples.ui.im.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zorro.kotlin.baselibs.imageloader.ImageLoaderKotlin
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.ui.im.bean.MessageType
import com.zorro.kotlin.samples.ui.im.bean.UIMessage


/**
 * Created by Zorro on 2019/12/16.
 * 备注：图片消息
 */
class ImageMessageItemProvider : BaseItemProvider<UIMessage, BaseViewHolder>() {
    override fun layout(): Int = R.layout.item_image_message

    override fun viewType(): Int = MessageType.IMAGE.value

    override fun convert(messageHolder: BaseViewHolder, uiMessage: UIMessage?, position: Int) {
        val message = uiMessage?.messageContent
        if (position % 3 == 1) {
            ImageLoaderKotlin.displayImage(
                mContext,
                messageHolder.getView(R.id.iv_image),
                "http://pic1.win4000.com/mobile/2019-11-28/5ddf2e6792dd6.jpg", R.mipmap.default_img
            )
        } else if (position % 3 == 2) {
            ImageLoaderKotlin.displayImage(
                mContext,
                messageHolder.getView(R.id.iv_image),
                "http://01.minipic.eastday.com/20170421/20170421143734_ba2eace48dd7194f8c64d545ad7c50ca_4.jpeg",
                R.mipmap.default_img
            )
        } else {
            ImageLoaderKotlin.displayImage(
                mContext,
                messageHolder.getView(R.id.iv_image),
                "http://00.minipic.eastday.com/20170427/20170427134513_e89e7fefe9493e353fe0b467a82dd9a7_7.jpeg",
                R.mipmap.default_img
            )
        }
        messageHolder.setText(R.id.tv_text_hint, message?.message?.body)
            .addOnClickListener(R.id.iv_image)
    }
}