package com.zorro.kotlin.samples.ui.im.adapter

import android.content.Context
import android.support.annotation.IntRange
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.zorro.kotlin.baselibs.utils.IMDateUtils
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.ui.im.bean.MessageDirection
import com.zorro.kotlin.samples.ui.im.bean.MessageType
import com.zorro.kotlin.samples.ui.im.bean.UIMessage
import kotlinx.android.synthetic.main.item_message_layout.view.*


/**
 * Created by Zorro on 2019/12/10.
 * 备注：聊天室消息列表
 * [MessageListAdapter]
 */
@Deprecated("请查看MessageListAdapter")
class MessageOldListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mData: ArrayList<UIMessage> = ArrayList()
    private var mContext: Context? = null
    private lateinit var mLayoutInflater: LayoutInflater


    fun setNewData(@Nullable data: ArrayList<UIMessage>) {
        this.mData = data
        notifyDataSetChanged()
    }

    fun addData(@IntRange(from = 0) position: Int, data: UIMessage) {
        mData.add(position, data)
        notifyItemInserted(position)
    }


    fun addData(data: UIMessage) {
        mData.add(data)
        notifyItemInserted(mData.size)
    }

    fun setData(@IntRange(from = 0) index: Int, data: UIMessage) {
        mData[index] = data
        notifyItemChanged(index)
    }

    fun addData(@IntRange(from = 0) position: Int, newData: Collection<UIMessage>) {
        mData.addAll(position, newData)
        notifyItemRangeInserted(position, newData.size)
    }

    fun addData(newData: Collection<UIMessage>) {
        mData.addAll(newData)
        notifyItemRangeInserted(mData.size - newData.size, newData.size)
    }

    fun remove(@IntRange(from = 0) position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mData.size - position)
    }

    override fun getItemViewType(position: Int): Int {
        return mData[position].messageContent?.messageType?.value
            ?: MessageType.TEXT.value
    }

    override fun getItemCount(): Int = mData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.mContext = parent.context
        this.mLayoutInflater = LayoutInflater.from(mContext)
        val rootView = mLayoutInflater.inflate(R.layout.item_message_layout, parent, false)
        when (viewType) {
            MessageType.TEXT.value -> {
                addMessageLayout(rootView, R.layout.item_text_message)
            }
            MessageType.IMAGE.value -> {
                addMessageLayout(rootView, R.layout.item_image_message)
            }
        }
        return BaseViewHolder(rootView)
    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val messageHolder: BaseViewHolder = viewHolder as BaseViewHolder
        val uiMessage = mData[position]
        val message = uiMessage.messageContent
        //时间展示规则
        val time: String =
            IMDateUtils.getConversationFormatDate(uiMessage.sentTime, mContext)
        if (position == 0) {
            messageHolder.setGone(R.id.tv_time, true)
                .setText(R.id.tv_time, time)
        } else {
            val preUIMessage = mData[position - 1]
            if (IMDateUtils.isShowChatTime(uiMessage.sentTime, preUIMessage.sentTime, 180)) {
                messageHolder.setGone(R.id.tv_time, true)
                    .setText(R.id.tv_time, time)
            } else {
                messageHolder.setGone(R.id.tv_time, false)
            }
        }
        //用户头像展示规则
        when (uiMessage.messageDirection ?: MessageDirection.RECEIVE) {
            MessageDirection.RECEIVE -> {
                messageHolder
                    .setGone(R.id.iv_left_user_avatar, true)
                    .setGone(R.id.iv_right_user_avatar, false)
            }
            MessageDirection.SEND -> {
                messageHolder
                    .setGone(R.id.iv_left_user_avatar, false)
                    .setGone(R.id.iv_right_user_avatar, true)
            }
        }

        when (viewHolder.itemViewType) {
            MessageType.TEXT.value -> {
                when (uiMessage.messageDirection ?: MessageDirection.RECEIVE) {
                    MessageDirection.RECEIVE -> {
                        messageHolder
                            .setBackgroundRes(R.id.tv_text_message, R.mipmap.left_duihuatiao)
                            .setTextColor(
                                R.id.tv_text_message,
                                ContextCompat.getColor(mContext!!, R.color.color_333333)
                            )
                            .setText(R.id.tv_text_message, message.message.body)
                    }
                    MessageDirection.SEND -> {
                        messageHolder
                            .setBackgroundRes(R.id.tv_text_message, R.mipmap.right_duihuatiao)
                            .setTextColor(
                                R.id.tv_text_message,
                                ContextCompat.getColor(mContext!!, R.color.white)
                            )
                            .setText(R.id.tv_text_message, message.message.body)
                    }
                }
            }
            MessageType.IMAGE.value -> {
                messageHolder
                    .setText(R.id.tv_text_hint, message.message.body)
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