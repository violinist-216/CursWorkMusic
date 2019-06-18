package com.example;

import com.example.Messages.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class MsgProducer {

    @Autowired
    @Qualifier("rabbitTemplateSinger")
    private RabbitTemplate rabbitSinger;

    @Autowired
    @Qualifier("rabbitTemplateSong")
    private RabbitTemplate rabbitSong;

    @Autowired
    @Qualifier("rabbitTemplateAlbum")
    private RabbitTemplate rabbitAlbum;

    @Autowired
    @Qualifier("rabbitTemplateProducer")
    private RabbitTemplate rabbitProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgProducer.class);

    public void sendMessage(Message message){
        RabbitTemplate rabbitTemplate = null;
        if(message.getClassName().equalsIgnoreCase("song"))
            rabbitTemplate = rabbitSong;
        else
        if(message.getClassName().equalsIgnoreCase("album"))
            rabbitTemplate = rabbitAlbum;
        else
        if(message.getClassName().equalsIgnoreCase("singer"))
            rabbitTemplate = rabbitSinger;
        else
            rabbitTemplate = rabbitProducer;
        sendMessage(rabbitTemplate, message);
    }

    private void sendMessage(RabbitTemplate rabbitTemplate, Message message){
        try {
            LOGGER.debug("<<<<< SENDING MESSAGE");
            rabbitTemplate.convertAndSend(message);
            LOGGER.debug(MessageFormat.format("MESSAGE SENT TO {0} >>>>>>", rabbitTemplate.getRoutingKey()));
        } catch (AmqpException e) {
            LOGGER.error("Error sending " + message.getClassName() + " message : " + e);
        }
    }

    public ObjectNode info()
    {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = factory.objectNode();
        root.put("host", rabbitSinger.getConnectionFactory().getHost());
        root.put("port", rabbitSinger.getConnectionFactory().getPort());
        root.put("Singer UUID", rabbitSinger.getUUID());
        root.put("Song UUID", rabbitSong.getUUID());
        root.put("Album UUID", rabbitAlbum.getUUID());
        root.put("Producer UUID", rabbitProducer.getUUID());
        root.put("queueSinger", rabbitSinger.getRoutingKey());
        root.put("queueSong", rabbitSong.getRoutingKey());
        root.put("queueAlbum", rabbitAlbum.getRoutingKey());
        root.put("queueProducer", rabbitProducer.getRoutingKey());


        return root;
    }
}
