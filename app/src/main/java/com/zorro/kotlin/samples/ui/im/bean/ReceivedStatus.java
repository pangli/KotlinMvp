package com.zorro.kotlin.samples.ui.im.bean;

/**
 * 消息接收状态
 */
public enum ReceivedStatus {
    DOWNLOADED(100),
    FAILED(200);


    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    ReceivedStatus(int value) {
        this.value = value;
    }
}
