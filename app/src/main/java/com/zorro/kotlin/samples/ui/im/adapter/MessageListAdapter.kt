package com.zorro.kotlin.samples.ui.im.adapter

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zorro.kotlin.baselibs.imageloader.ImageLoaderKotlin
import com.zorro.kotlin.baselibs.utils.IMDateUtils
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.UserInfo
import com.zorro.kotlin.samples.ui.im.bean.MessageDirection
import com.zorro.kotlin.samples.ui.im.bean.MessageType
import com.zorro.kotlin.samples.ui.im.bean.UIMessage
import kotlinx.android.synthetic.main.item_message_layout.view.*

/**
 * Created by Zorro on 2019/12/12.
 * 备注：聊天室消息列表
 * [MessageAdapter]
 */
@Deprecated("请查看MessageAdapter")
class MessageListAdapter(val userInfo: UserInfo?) :
    BaseQuickAdapter<UIMessage, BaseViewHolder>(R.layout.item_message_layout) {


    override fun getItemViewType(position: Int): Int {
        return data[position].messageContent?.messageType?.value
            ?: MessageType.TEXT.value
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val rootView = mLayoutInflater.inflate(R.layout.item_message_layout, parent, false)
        when (viewType) {
            MessageType.TEXT.value -> {
                addMessageLayout(rootView, R.layout.item_text_message)
            }
            MessageType.IMAGE.value -> {
                //addMessageLayout(rootView, R.layout.item_image_message)
            }
        }
        return super.createBaseViewHolder(rootView)
    }

    override fun convert(messageHolder: BaseViewHolder, uiMessage: UIMessage?) {
        val message = uiMessage?.messageContent
        //时间展示规则
        val time: String = IMDateUtils.getConversationFormatDate(uiMessage?.sentTime ?: 0, mContext)
        val position = messageHolder.layoutPosition
        if (position == 0) {
            messageHolder.setGone(R.id.tv_time, true)
                .setText(R.id.tv_time, time)
        } else {
            val preUIMessage = mData[position - 1]
            if (IMDateUtils.isShowChatTime(uiMessage?.sentTime ?: 0, preUIMessage.sentTime, 180)) {
                messageHolder.setGone(R.id.tv_time, true)
                    .setText(R.id.tv_time, time)
            } else {
                messageHolder.setGone(R.id.tv_time, false)
            }
        }
        //用户头像展示规则
        when (uiMessage?.messageDirection ?: MessageDirection.RECEIVE) {
            MessageDirection.RECEIVE -> {
                messageHolder
                    .setGone(R.id.iv_left_user_avatar, true)
                    .setGone(R.id.iv_right_user_avatar, false)
            }
            MessageDirection.SEND -> {
                messageHolder
                    .setGone(R.id.iv_left_user_avatar, false)
                    .setGone(R.id.iv_right_user_avatar, true)
                if (userInfo?.full_head.isNullOrBlank()) {
                    ImageLoaderKotlin.displayUserIcon(
                        mContext,
                        messageHolder.getView(R.id.iv_right_user_avatar),
                        R.mipmap.morentouxiang
                    )
                } else {
                    ImageLoaderKotlin.displayUserIcon(
                        mContext,
                        messageHolder.getView(R.id.iv_right_user_avatar),
                        userInfo?.full_head
                    )
                }
            }
        }

        when (messageHolder.itemViewType) {
            MessageType.TEXT.value -> {
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
            MessageType.IMAGE.value -> {
                //messageHolder.setText(R.id.tv_text_hint, message?.message?.body)
            }
        }
    }


    /**
     * 添加消息样式布局
     */
    private fun addMessageLayout(rootView: View, @LayoutRes resourceId: Int) {
        val messageView = mLayoutInflater.inflate(
            resourceId,
            rootView.message_container,
            false
        )
        rootView.message_container.addView(messageView)
    }

}