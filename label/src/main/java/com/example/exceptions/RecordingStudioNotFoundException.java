package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecordingStudioNotFoundException extends RuntimeException{
    public RecordingStudioNotFoundException(Integer id) {
        super("Could not find recording studio with ID: " + id);
    }
}
