package com.zorro.kotlin.samples.ui.im.bean;

/**
 * 消息来源
 */
public enum MessageDirection {
    SEND(1),
    RECEIVE(2);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    MessageDirection(int value) {
        this.value = value;
    }
}
