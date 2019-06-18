package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SingerNotFoundException extends RuntimeException {
    public SingerNotFoundException(Integer id) {
        super("Could not find singer with ID: " + id);
    }
}
