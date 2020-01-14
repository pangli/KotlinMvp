package com.zorro.kotlin.samples.ui.im.provider

import android.support.v4.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.ui.im.bean.MessageDirection
import com.zorro.kotlin.samples.ui.im.bean.MessageType
import com.zorro.kotlin.samples.ui.im.bean.UIMessage


/**
 * Created by Zorro on 2019/12/16.
 * 备注：文字消息
 */
class TextMessageItemProvider : BaseItemProvider<UIMessage, BaseViewHolder>() {
    override fun layout(): Int = R.layout.item_text_message

    override fun viewType(): Int = MessageType.TEXT.value

    override fun convert(messageHolder: BaseViewHolder, uiMessage: UIMessage?, position: Int) {
        val message = uiMessage?.messageContent
        when (uiMessage?.messageDirection ?: MessageDirection.RECEIVE) {
            MessageDirection.RECEIVE -> {
                messageHolder
                    .setBackgroundRes(R.id.tv_text_message, R.mipmap.left_duihuatiao)
                    .setTextColor(
                        R.id.tv_text_message,
                        ContextCompat.getColor(mContext!!, R.color.color_333333)
                    )
                    .setText(R.id.tv_text_message, message?.message?.body)
                    .addOnClickListener(R.id.tv_text_message)
            }
            MessageDirection.SEND -> {
                messageHolder
                    .setBackgroundRes(R.id.tv_text_message, R.mipmap.right_duihuatiao)
                    .setTextColor(
                        R.id.tv_text_message,
                        ContextCompat.getColor(mContext!!, R.color.white)
                    )
                    .setText(R.id.tv_text_message, message?.message?.body)
                    .addOnClickListener(R.id.tv_text_message)
            }
        }
    }
}