package com.zorro.kotlin.samples.xmpp;

import com.orhanobut.logger.Logger;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Zorro
 * @date 2019/9/19 0019 14:30
 * Xmpp即时通讯初始化
 */
public class XmppManager extends XMPPTCPConnection {
    private static volatile XmppManager connection;
    private Lock connectionLock = new ReentrantLock();
    private static int SERVER_PORT = 5222;
    private static String SERVER_HOST = "47.103.144.208";
    private static String SERVER_NAME = "izuf64jq4bbagoh6cy0x9iz";
    private ConnectionListener connectionListener;
    private PingManager pingManager;

    public XmppManager(XMPPTCPConnectionConfiguration config) {
        super(config);
    }

    /**
     * 单例模式
     *
     * @return XmppManager
     */
    public static XmppManager getInstance() {
        if (connection == null) {
            synchronized (XmppManager.class) {
                if (connection == null) {
                    initXmppManager();
                }
            }
        }
        return connection;
    }

    /**
     * 初始化配置
     */
    private static void initXmppManager() {
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    //设置连接超时的最大时间
                    .setConnectTimeout(10000)
                    //设置安全模式,禁用SSL连接
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    //设置客服端类型
                    .setResource("Android")
                    //设置openfire服务器名称
                    .setXmppDomain(SERVER_NAME)
                    //设置openfire主机IP
                    .setHostAddress(InetAddress.getByName(SERVER_HOST))
                    //设置端口号：默认5222
                    .setPort(SERVER_PORT)
                    //设置离线状态
                    .setSendPresence(false)
                    //设置开启压缩，可以节省流量
                    .setCompressionEnabled(true)
                    .build();
            //设置是否查看日志
            SmackConfiguration.DEBUG = true;
            connection = new XmppManager(config);
            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            // 重联间隔5秒
            reconnectionManager.setFixedDelay(5);
            reconnectionManager.enableAutomaticReconnection();//开启重联机制
            // 自动回复回执方法，如果对方的消息要求回执。
//            ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
//            ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());
//            DeliveryReceiptManager deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(connection);
//            deliveryReceiptManager.autoAddDeliveryReceiptRequests();
//            deliveryReceiptManager.addReceiptReceivedListener(
//                    (fromJid, toJid, receiptId, receipt) ->
//                            Logger.d("fromJid==" + fromJid.toString() + "\ntoJid==" + toJid.toString()
//                                    + "\nreceiptId==" + receiptId + "\nreceipt==" + receipt.toXML(null)));
        } catch (XmppStringprepException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接openfire
     *
     * @param listener 设置连接监听，用来连接成功后获取聊天室
     */
    public void connectOpenfire(ConnectionListener listener) {
        this.connectionListener = listener;
        if (connection == null) {
            getInstance();
        }
        // 添加连接监听
        connection.addConnectionListener(connectionListener);
        new Thread(() -> {
            try {
                connectionLock.lock();
                if (!checkConnection()) {
                    connection.connect();
                }
            } catch (IOException | InterruptedException | XMPPException | SmackException e) {
                e.printStackTrace();
            } finally {
                connectionLock.unlock();
            }
        }).start();
    }

    /**
     * 失败重连
     */
    public void failedReconnection() {
        if (connection == null) {
            getInstance();
        }
        new Thread(() -> {
            try {
                connectionLock.lock();
                if (!checkConnection()) {
                    connection.connect();
                }
            } catch (IOException | InterruptedException | XMPPException | SmackException e) {
                e.printStackTrace();
            } finally {
                connectionLock.unlock();
            }
        }).start();
    }

    /**
     * 聊天室连接成功后维持ping
     */
    public void settingPing() {
        PingManager.setDefaultPingInterval(10);
        pingManager = PingManager.getInstanceFor(connection);
        pingManager.registerPingFailedListener(pingListener);
    }

    /**
     * ping监听
     */
    private PingFailedListener pingListener = () -> Logger.e("xmpp--pingFailed");

    /**
     * xmpp登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public void loginXmpp(String userName, String password) {
        try {
            if (!checkAuthenticated()) {
                connection.login(userName, password);
                setPresence(0);
            }
        } catch (IOException | InterruptedException | SmackException | XMPPException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否已连接
     *
     * @return
     */
    public boolean checkConnection() {
        return null != connection && connection.isConnected();
    }

    /**
     * 判断是否已经通过身份验证，是否已经完成登录
     *
     * @return
     */
    public boolean checkAuthenticated() {
        return checkConnection() && connection.isAuthenticated();
    }

    /**
     * 关闭连接
     *
     * @return
     */
    public void closeConnection() {
        if (checkConnection()) {
            new Thread(() -> {
                if (pingManager != null) {
                    pingManager.unregisterPingFailedListener(pingListener);
                }
                connection.removeConnectionListener(connectionListener);
                connection.setPresence(5);
                connection.disconnect();
                connection = null;
                connectionListener = null;
                Logger.d("xmpp--关闭连接");
            }).start();
        }

    }

    /**
     * 判断用户是否在线
     *
     * @param jid
     * @return 0代表不在线，1代表在线
     */
    public int isUserOnLine(String jid) {
        Roster roster = Roster.getInstanceFor(connection);
        try {
            Presence presence = roster.getPresence(JidCreate.bareFrom(jid));
            if (presence.isAvailable()) {
                return 1;
            } else {
                return 0;
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取聊天窗口
     *
     * @return ChatManager
     */
    public ChatManager getChatManager() {
        if (checkAuthenticated()) {
            return ChatManager.getInstanceFor(connection);
        }
        return null;
    }

    /**
     * 获取聊天对象
     *
     * @param JID JID
     * @return Chat
     */
    public Chat getFriendChat(ChatManager chatManager, String JID) {
        try {
            if (chatManager != null) {
                return chatManager.chatWith(JidCreate.entityBareFrom(JID));
            }
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送单人聊天消息
     *
     * @param chat    chat
     * @param message 消息对象
     */
    public void sendSingleMessage(Chat chat, Message message) {
        try {
            chat.send(message);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送单人聊天消息
     *
     * @param chat    chat
     * @param message 消息文本
     */
    public void sendSingleMessage(Chat chat, String message) {
        try {
            chat.send(message);
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取离线消息
     *
     * @return
     */
    public void getOfflineMessage() {
        if (checkAuthenticated()) {
            try {
                OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
                List<Message> messageList = offlineManager.getMessages();
                setPresence(0);
                offlineManager.deleteMessages();
            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 更改用户状态
     */
    public void setPresence(int code) {
        if (checkAuthenticated()) {
            Presence presence;
            try {
                switch (code) {
                    case 0:
                        presence = new Presence(Presence.Type.available);
                        connection.sendStanza(presence);
                        Logger.d("xmpp--设置在线");
                        break;
                    case 1:
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.chat);
                        connection.sendStanza(presence);
                        Logger.d("xmpp--设置Q我吧");
                        break;
                    case 2:
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.dnd);
                        connection.sendStanza(presence);
                        Logger.d("xmpp--设置忙碌");
                        break;
                    case 3:
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.away);
                        connection.sendStanza(presence);
                        Logger.d("xmpp--设置离开");
                        break;
                    case 4:
                    case 5:
                        presence = new Presence(Presence.Type.unavailable);
                        connection.sendStanza(presence);
                        Logger.d("xmpp--设置离线");
                        break;
                    default:
                        break;
                }
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
