package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProducerNotFoundException extends RuntimeException {
    public ProducerNotFoundException(Integer id) {
        super("Could not find  producer with ID: " + id);
    }
}
