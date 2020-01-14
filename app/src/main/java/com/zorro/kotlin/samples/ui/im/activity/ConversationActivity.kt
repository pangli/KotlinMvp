package com.zorro.kotlin.samples.ui.im.activity

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View

import com.gyf.immersionbar.ImmersionBar
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter
import com.zorro.kotlin.baselibs.utils.KeyBoardUtil
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.mvp.contract.TestContract
import com.zorro.kotlin.samples.mvp.presenter.TestPresenter
import com.zorro.kotlin.samples.ui.base.ZorroBaseMvpActivity
import com.zorro.kotlin.samples.ui.im.adapter.MessageAdapter
import com.zorro.kotlin.samples.ui.im.bean.*
import com.zorro.kotlin.samples.xmpp.XmppManager
import kotlinx.android.synthetic.main.include_message_list_layout.*
import kotlinx.android.synthetic.main.include_send_message_bar_layout.*
import kotlinx.android.synthetic.main.include_toolbar_layout.*

import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.Message

/**
 * Created by Zorro on 2019/12/9.
 * 备注：聊天会话界面
 */
class ConversationActivity : ZorroBaseMvpActivity<TestContract.View, TestContract.Presenter>(),
    TestContract.View {

    private var adapter: MessageAdapter? = null
    private var chatManager: ChatManager? = null
    private var chat: Chat? = null


    override fun createPresenter(): TestContract.Presenter = TestPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_conversation_layout
    override fun initView() {
        super.initView()
        ImmersionBar.with(this)
            .titleBar(toolbar)
            .navigationBarColorInt(Color.WHITE)
            .statusBarDarkFont(true, 0.2f)
            .navigationBarDarkIcon(true, 0.2f)
            .keyboardEnable(true)
            .init()
        tv_title.text = getString(R.string.str_dedicated_consultant)
        initRefreshLayout()
    }

    override fun bindListener() {
        iv_left.setOnClickListener(onClickListener)
        btn_send.setOnClickListener(onClickListener)
        edit_message.addTextChangedListener(textWatcher)
    }

    override fun initData() {
        super.initData()
        adapter = MessageAdapter(userInfo)
        adapter?.bindToRecyclerView(recyclerView)
        val message = Message()
        message.body = "hi~请问您有什么疑问？"
        val uiMessage = UIMessage(
            "-1",
            MessageDirection.RECEIVE,
            SentStatus.SENT,
            System.currentTimeMillis(),
            MessageContent(message, MessageType.TEXT)
        )
        adapter?.addData(uiMessage)
        recyclerViewScrollToBottom()
    }

    override fun start() {
        //getTestData(200)
        adapter?.setOnItemClickListener { adapter, view, position ->
            KeyBoardUtil.closeKeyBord(edit_message, this@ConversationActivity)
        }
        chatConnection()
    }


    /**
     * 初始化刷新布局
     */
    private fun initRefreshLayout() {
        refreshLayout.setEnableRefresh(false)//必须关闭
        refreshLayout.setEnableAutoLoadMore(true)//必须关闭
        refreshLayout.setEnableNestedScroll(false)//必须关闭
        refreshLayout.setEnableScrollContentWhenLoaded(true)//必须关闭
        refreshLayout.layout.scaleY = -1f//必须设置
        refreshLayout.setScrollBoundaryDecider(object : ScrollBoundaryDeciderAdapter() {
            override fun canLoadMore(content: View): Boolean {
                return super.canRefresh(content) //必须替换
            }
        })
        recyclerView.setOnTouchListener { v, event ->
            val mPressedView = recyclerView.findChildViewUnder(event.x, event.y)
            if (mPressedView == null) {
                KeyBoardUtil.closeKeyBord(edit_message, this@ConversationActivity)
            }
            false
        }
        recyclerView.addOnLayoutChangeListener(onLayoutChangeListener)
        //监听加载，而不是监听 刷新
        refreshLayout.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                refreshLayout.finishLoadMore()
            }, 100)
        }
    }

    /**
     * 聊天室连接
     */
    private fun chatConnection() {
        XmppManager.getInstance().connectOpenfire(
            (object : ConnectionListener {
                override fun connected(connection: XMPPConnection?) {
                    Logger.d("xmpp--已经连接")
                    Logger.d("xmpp--当前线路：" + connection!!.host)
//                    if (!userInfo?.imUsername.isNullOrBlank() && !userInfo?.imPwd.isNullOrBlank())
//                        XmppManager.getInstance().loginXmpp(userInfo?.imUsername, userInfo?.imPwd)
                    XmppManager.getInstance().loginXmpp("1154747", "45487d4a4d")
                }

                override fun connectionClosed() {
                    Logger.d("xmpp--连接关闭")
                }

                override fun connectionClosedOnError(e: Exception?) {
                    Logger.d("xmpp--Exception: " + e.toString())
                    Logger.d("xmpp--连接错误")
                }

                override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
                    Logger.d("xmpp--已经登录")
                    chatAuthenticated()
                }

            })
        )
    }

    /**
     * 聊天室登录成功
     */
    private fun chatAuthenticated() {
        XmppManager.getInstance().settingPing()
        chatManager = XmppManager.getInstance().chatManager
        //收消息
        chatManager?.addIncomingListener(incomingListeners)
        //发消息
        chatManager?.addOutgoingListener(outgoingListeners)
//        if (!userInfo?.imSupport.isNullOrBlank() && chatManager != null)
//            chat = XmppManager.getInstance()
//                .getFriendChat(chatManager, userInfo?.imSupport)
        chat = XmppManager.getInstance()
            .getFriendChat(chatManager, "154575488")
    }

    /**
     * 收消息监听
     */
    private val incomingListeners = IncomingChatMessageListener { from, message, chat ->
        Logger.d("收到消息--${Thread.currentThread().name}--" + message.toXML(null))
        val uiMessage = UIMessage(
            message.stanzaId,
            MessageDirection.RECEIVE,
            SentStatus.SENT,
            System.currentTimeMillis(),
            MessageContent(message, MessageType.TEXT)
        )
        recyclerView.post {
            adapter?.addData(uiMessage)
            recyclerViewScrollToBottom()
        }
    }
    /**
     * 发消息监听
     */
    private val outgoingListeners = OutgoingChatMessageListener { to, message, chat ->
        Logger.d("发出消息--${Thread.currentThread().name}--" + message.toXML(null))
        val uiMessage = UIMessage(
            message.stanzaId,
            MessageDirection.SEND,
            SentStatus.SENT,
            System.currentTimeMillis(),
            MessageContent(message, MessageType.TEXT)
        )
        recyclerView.post {
            adapter?.addData(uiMessage)
            recyclerViewScrollToBottom()
        }
    }
    /**
     * 解决软键盘弹出时聊天列被挡住问题
     */
    private val onLayoutChangeListener =
        View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                recyclerViewScrollToBottom()
            }
        }
    /**
     * 输入监听
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            //输入后的监听
            val content = s.toString().trim()
            if (!content.isNotEmpty()) {//空
                btn_send.isEnabled = false
                btn_send.setTextColor(
                    ContextCompat.getColor(
                        this@ConversationActivity,
                        R.color.color_cccccc
                    )
                )
            } else {//非空
                btn_send.isEnabled = true
                btn_send.setTextColor(
                    ContextCompat.getColor(
                        this@ConversationActivity,
                        R.color.color_391bbb
                    )
                )
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //输入前的监听
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //输入文字产生变化的监听
        }
    }
    /**
     * 点击事件绑定
     */
    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_left -> {
                finish()
            }
            R.id.btn_send -> {
                if (chat != null) {
//                    val stanza = Message()
//                    stanza.body = edit_message.text.toString().trim()
//                    stanza.type = Message.Type.chat
//                    // 添加回执请求
//                    DeliveryReceiptRequest.addTo(stanza)
//                    XmppManager.getInstance()
//                        .sendSingleMessage(chat, stanza)
                    XmppManager.getInstance()
                        .sendSingleMessage(chat, edit_message.text.toString().trim())
                    edit_message.setText("")
                }
                ////////////////////测试展示代码///////////////////
                val message = Message()
                message.body = edit_message.text.toString().trim()
                val uiMessage = UIMessage(
                    "message.stanzaId",
                    MessageDirection.SEND,
                    SentStatus.SENT,
                    System.currentTimeMillis(),
                    MessageContent(message, MessageType.TEXT)
                )
                recyclerView.post {
                    adapter?.addData(uiMessage)
                    recyclerViewScrollToBottom()
                }
                //////////////////////////////////////
            }

        }
    }

    /**
     * recyclerView滚动到低
     */
    private fun recyclerViewScrollToBottom() {
        if ((adapter?.itemCount ?: 0) > 0) {
            recyclerView.smoothScrollToPosition((adapter?.itemCount ?: 0) - 1)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            onUserInteraction()
        }
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    override fun onPause() {
        KeyBoardUtil.closeKeyBord(edit_message, this)
        super.onPause()
    }

    override fun onDestroy() {
        chatManager?.removeOutgoingListener(outgoingListeners)
        chatManager?.removeIncomingListener(incomingListeners)
        edit_message.removeTextChangedListener(textWatcher)
        recyclerView.removeOnLayoutChangeListener(onLayoutChangeListener)
        XmppManager.getInstance().closeConnection()
        super.onDestroy()
    }

    /**
     * 测试多布局数据
     */
/*    private fun getTestData(day: Int): ArrayList<UIMessage> {
        val data = ArrayList<UIMessage>()
        val time = System.currentTimeMillis() - day * 3600 * 1000
        for (i in 1..day) {
            var uiMessage: UIMessage?
            if (i % 3 == 1) {
                val message = Message()
                message.body = "我收到的消息---$i"
                uiMessage = UIMessage(
                    "", MessageDirection.RECEIVE, SentStatus.SENT, time + i * 1000 * 3600,
                    MessageContent(message, MessageType.TEXT)
                )

            } else if (i % 3 == 2) {
                val message = Message()
                message.body = "我发出去的消息,我发出去的消息,我发出去的消息,我发出去的消息------$i"
                uiMessage = UIMessage(
                    "", MessageDirection.SEND, SentStatus.SENT, time + i * 1000 * 3600,
                    MessageContent(message, MessageType.TEXT)
                )


            } else {
                val message = Message()
                message.body = "测试多布局---------$i"
                data.add(
                    UIMessage(
                        "", MessageDirection.RECEIVE, SentStatus.SENT, time + i * 1000 * 3600,
                        MessageContent(message, MessageType.IMAGE)
                    )
                )
                uiMessage = UIMessage(
                    "", MessageDirection.SEND, SentStatus.SENT, time + i * 1000 * 3600,
                    MessageContent(message, MessageType.IMAGE)
                )
            }
            data.add(uiMessage)
        }
        adapter?.setNewData(data)
        if ((adapter?.itemCount ?: 0) > 0)
            recyclerView.scrollToPosition((adapter?.itemCount ?: 0) - 1)
        return data
    }*/
}