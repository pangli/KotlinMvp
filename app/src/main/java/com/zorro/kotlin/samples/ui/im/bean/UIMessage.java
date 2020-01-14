package com.zorro.kotlin.samples.ui.im.bean;

import java.io.Serializable;

/**
 * Created by Zorro on 2019/12/10.
 * 备注：消息UI处理
 */
public class UIMessage implements Serializable {
    private String messageId;
    private MessageDirection messageDirection;
    private ReceivedStatus receivedStatus;
    private SentStatus sentStatus;
    private long sentTime;
    private MessageContent messageContent;
    private boolean isChecked;

    public UIMessage(String messageId, MessageDirection messageDirection, SentStatus sentStatus, long sentTime, MessageContent messageContent) {
        this.messageId = messageId;
        this.messageDirection = messageDirection;
        this.sentStatus = sentStatus;
        this.sentTime = sentTime;
        this.messageContent = messageContent;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageDirection getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(MessageDirection messageDirection) {
        this.messageDirection = messageDirection;
    }

    public ReceivedStatus getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(ReceivedStatus receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public SentStatus getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(SentStatus sentStatus) {
        this.sentStatus = sentStatus;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
