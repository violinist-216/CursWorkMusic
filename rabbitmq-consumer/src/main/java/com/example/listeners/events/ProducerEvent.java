package com.example.listeners.events;

import com.example.Messages.Message;
import org.springframework.context.ApplicationEvent;

public class ProducerEvent extends ApplicationEvent {
    private Message msg;

    public ProducerEvent(Object source, Message msg) {
        super(source);
        this.msg = msg;
    }

    public Message getMessage() {
        return msg;
    }

}
