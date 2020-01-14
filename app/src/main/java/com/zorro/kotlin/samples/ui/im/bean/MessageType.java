package com.zorro.kotlin.samples.ui.im.bean;

/**
 * 消息类型
 */
public enum MessageType {
    TEXT(1000),
    IMAGE(2000),
    VIDEO(3000);


    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    MessageType(int value) {
        this.value = value;
    }
}
