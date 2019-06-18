package com.example.exceptions;

import com.example.Album;
import com.example.Singer;
import com.example.Song;

public class SingerNotHaveAlbumsException extends RuntimeException {
    public SingerNotHaveAlbumsException(Singer singer, Album album) {
        super("Singer: " + singer.toString() + " does not contain album: " + album.toString());
    }

    public SingerNotHaveAlbumsException(Integer singerId, Integer albumId) {
        super("Singer with id: " + singerId + " does not contain albums with id: " + albumId);
    }
}
