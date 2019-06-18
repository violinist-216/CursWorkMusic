package com.example;

import com.example.Messages.*;
import com.example.listeners.events.AlbumEvent;
import com.example.listeners.events.ProducerEvent;
import com.example.listeners.events.SingerEvent;
import com.example.listeners.events.SongEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConsumerController {
    private List<SseEmitter> lsEmitters = new ArrayList<SseEmitter>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private MessageRepository msgRepo;

    @EventListener({SingerEvent.class})
    public void handleSingerEvt(SingerEvent evt) {
        System.out.println("EVENT RECEIVED: " + evt.getMessage().getDescription());        
        msgRepo.save(evt.getMessage());
        List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
        this.lsEmitters.forEach(emitter -> {
            try {
                emitter.send(evt.getMessage());
            } catch (Exception e) {
                LOGGER.error("Error ", e);
                deadEmitters.add(emitter);
            }
        });

        this.lsEmitters.removeAll(deadEmitters);
    }

    @EventListener({SongEvent.class})
    public void handleSongEvt(SongEvent evt) {
        System.out.println("EVENT RECEIVED: " + evt.getMessage().getDescription());

        this.msgRepo.save(evt.getMessage());

        List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
        this.lsEmitters.forEach(emitter -> {
            try {
                emitter.send(evt.getMessage());
            } catch (Exception e) {
                LOGGER.error("Error ", e);
                deadEmitters.add(emitter);
            }
        });

        this.lsEmitters.removeAll(deadEmitters);
    }

    @EventListener({AlbumEvent.class})
    public void handleAlbumEvt(AlbumEvent evt) {
        System.out.println("EVENT RECEIVED: " + evt.getMessage().getDescription());
        msgRepo.save(evt.getMessage());
        List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
        this.lsEmitters.forEach(emitter -> {
            try {
                emitter.send(evt.getMessage());
            } catch (Exception e) {
                LOGGER.error("Error ", e);
                deadEmitters.add(emitter);
            }
        });
    }

    @EventListener({ProducerEvent.class})
    public void handleProducerEvt(ProducerEvent evt) {
        System.out.println("EVENT RECEIVED: " + evt.getMessage().getDescription());
        msgRepo.save(evt.getMessage());
        List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
        this.lsEmitters.forEach(emitter -> {
            try {
                emitter.send(evt.getMessage());
            } catch (Exception e) {
                LOGGER.error("Error ", e);
                deadEmitters.add(emitter);
            }
        });
    }
}
