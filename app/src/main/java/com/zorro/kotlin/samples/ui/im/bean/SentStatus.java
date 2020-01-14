package com.zorro.kotlin.samples.ui.im.bean;

/**
 * 消息发送状态
 */
public enum SentStatus {
    SENDING(10),
    FAILED(20),
    SENT(30);


    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    SentStatus(int value) {
        this.value = value;
    }
}
