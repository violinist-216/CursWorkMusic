package com.example.exceptions;

import com.example.Singer;

public class SingerNotHaveProducerException extends RuntimeException {
    public SingerNotHaveProducerException(Singer singer) {
        super("Singer: " + singer.toString() + " does not contain producer" );
    }

    public SingerNotHaveProducerException(Integer singerId) {
        super("Singer with id: " + singerId + " does not contain producer");
    }
}
