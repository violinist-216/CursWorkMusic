package com.example.exceptions;

import com.example.Album;
import com.example.Singer;
import com.example.Song;

public class AlbumNotHaveSongsException extends RuntimeException {
    public AlbumNotHaveSongsException(Album album, Song song) {
        super("Singer: " + album.toString() + " does not contain song: " + song.toString());
    }

    public AlbumNotHaveSongsException(Integer albumId, Integer songId) {
        super("Album with id: " + albumId + " does not contain student with id: " + songId);
    }
}
