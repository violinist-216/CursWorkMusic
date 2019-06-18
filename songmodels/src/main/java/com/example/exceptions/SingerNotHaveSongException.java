package com.example.exceptions;

import com.example.Singer;
import com.example.Song;

public class SingerNotHaveSongException extends RuntimeException {
    public SingerNotHaveSongException(Singer singer, Song song) {
        super("Singer: " + singer.toString() + " does not contain song" + song.toString());
    }

    public SingerNotHaveSongException(Integer singerId, Integer songId) {
        super("Singer with id: " + singerId + " does not contain song with Id: " + songId);
    }
}
