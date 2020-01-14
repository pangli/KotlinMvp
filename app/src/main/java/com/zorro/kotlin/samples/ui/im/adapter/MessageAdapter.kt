package com.zorro.kotlin.samples.ui.im.adapter

import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.zorro.kotlin.baselibs.adapter.IMAdapter
import com.zorro.kotlin.baselibs.imageloader.ImageLoaderKotlin
import com.zorro.kotlin.baselibs.utils.IMDateUtils
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.UserInfo
import com.zorro.kotlin.samples.ui.im.bean.MessageDirection
import com.zorro.kotlin.samples.ui.im.bean.MessageType
import com.zorro.kotlin.samples.ui.im.bean.UIMessage
import com.zorro.kotlin.samples.ui.im.provider.ImageMessageItemProvider
import com.zorro.kotlin.samples.ui.im.provider.TextMessageItemProvider
import kotlinx.android.synthetic.main.item_message_layout.view.*

/**
 * Created by Zorro on 2019/12/16.
 * 备注：聊天室消息列表
 */
class MessageAdapter(val userInfo: UserInfo?) :
    IMAdapter<UIMessage, BaseViewHolder>(R.layout.item_message_layout) {
    init {
        finishInitialize()
    }

    /**
     * 添加Item的子View
     */
    override fun addItemChildView(rootView: View, multiTypeLayoutResId: Int) {
        rootView.message_container.addView(
            mLayoutInflater.inflate(
                multiTypeLayoutResId,
                rootView.message_container,
                false
            )
        )
    }

    /**
     * 根据实体类判断并返回对应的viewType
     */

    override fun getViewType(t: UIMessage?): Int = t?.messageContent?.messageType?.value
        ?: MessageType.TEXT.value

    /**
     * 注册相关的条目provider
     */
    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(TextMessageItemProvider())
        mProviderDelegate.registerProvider(ImageMessageItemProvider())
    }


    /**
     * itemView公共View数据绑定
     */
    override fun convert(messageHolder: BaseViewHolder, uiMessage: UIMessage?) {
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
        super.convert(messageHolder, uiMessage)
    }
}