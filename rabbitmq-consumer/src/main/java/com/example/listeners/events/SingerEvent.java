package com.example.listeners.events;

import com.example.Messages.Message;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class SingerEvent extends ApplicationEvent {

    private Message msg;

    public SingerEvent(Object source, Message msg) {
        super(source);
        this.msg = msg;
    }

    public Message getMessage() {
        return msg;
    }



}
