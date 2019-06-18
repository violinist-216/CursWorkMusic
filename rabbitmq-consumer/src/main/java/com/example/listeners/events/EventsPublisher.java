package com.example.listeners.events;

import com.example.Messages.Message;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class EventsPublisher implements ApplicationEventPublisherAware {

    protected ApplicationEventPublisher appPublisher;

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher appPublisher) {

        this.appPublisher = appPublisher;
    }

    public void publishSinger(Message message)
    {
        SingerEvent evt = new SingerEvent(this, message);
        appPublisher.publishEvent(evt);
    }

    public void publishSong(Message message)
    {
        SongEvent evt = new SongEvent(this, message);
        appPublisher.publishEvent(evt);
    }

    public void publishAlbum(Message message)
    {
        AlbumEvent evt = new AlbumEvent(this, message);
        appPublisher.publishEvent(evt);
    }

    public void publishProducer(Message message)
    {
        ProducerEvent evt = new ProducerEvent(this, message);
        appPublisher.publishEvent(evt);
    }
}
