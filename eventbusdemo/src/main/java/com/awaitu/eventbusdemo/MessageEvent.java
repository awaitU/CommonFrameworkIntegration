package com.awaitu.eventbusdemo;

/**
 * Created by lucky on 2019/1/6.
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message+"lucky lucky!";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}