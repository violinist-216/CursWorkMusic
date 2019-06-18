package com.example.exceptions;

import com.example.Producer;
import com.example.Singer;

public class ProducerNotHaveSingersException extends RuntimeException {
    public ProducerNotHaveSingersException(Producer producer, Singer singer) {
        super("Producer: " + producer.toString() + " does not contain singers: " + singer.toString());
    }

    public ProducerNotHaveSingersException(Integer producerId) {
        super("Producer with id: " + producerId + " does not contain singer" );
    }
}
