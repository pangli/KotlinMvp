package com.zorro.kotlin.samples.ui.im.bean;

import org.jivesoftware.smack.packet.Message;

import java.io.Serializable;

/**
 * Created by Zorro on 2019/12/10.
 * 备注：消息内容数据
 */
public class MessageContent implements Serializable {
    private Message message;
    private MessageType messageType;

    public MessageContent(Message message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
