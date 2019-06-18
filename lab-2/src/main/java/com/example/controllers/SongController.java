package com.example.controllers;

import com.example.Album;
import com.example.Singer;
import com.example.Song;
import com.example.services.AlbumService;
import com.example.SingerService;
import com.example.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private SingerService singerService;

    @GetMapping
    public Collection<Song> getSongs() {
        return songService.getAll();
    }

    @GetMapping("/{songId}")
    public Song getSong(@PathVariable Integer songId) {
        return songService.getObjectById(songId);
    }

    @PostMapping
    public Song createSong(@Valid @RequestBody Song newSong) {
        return songService.saveObject(newSong);
    }
    @PutMapping("/{songId}")
    public Song updateSong(@Valid @RequestBody Song updatedSong, @PathVariable Integer songId) {
        return songService.updateObject(updatedSong, songId);
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<?> deleteSong(@PathVariable Integer songId) {
        try {
            changeSongAlbum(songId, -2);
            changeSongSinger(songId, -2);
            songService.deleteObject(songId);
        } catch (Exception ex) {
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    //////////////////////////////
    @PostMapping("/{songId}/album/{newAlbumId}")
    public Collection<Song> changeSongAlbum(@PathVariable Integer songId, @PathVariable Integer newAlbumId) {
        Song song = songService.getObjectById(songId);
        removeSongFromAlbum(song);
        return addSongToAlbum(song, newAlbumId);
    }
    private void removeSongFromAlbum(Song song) {
        if (song.getAlbum() != null) {
            Album songAlbum = albumService.getObjectById(song.getAlbum().getId());
            songAlbum.removeSong(song);
            songAlbum.setSongs(songAlbum.getSongs());
            albumService.saveObject(songAlbum);
            songService.saveObject(song);
        }
    }
    private Collection<Song> addSongToAlbum(Song song, Integer albumId) {
        Song son = songService.getObjectById(song.getId());
        if (albumId == -2)
            return null;
        Album album = albumService.getObjectById(albumId);
        son.setAlbum(album);
        album.addSong(son);
        album.setSongs(album.getSongs());
        albumService.saveObject(album);
        songService.saveObject(son);
        return getSongsOfAlbum(albumId);
    }
    private Collection<Song> getSongsOfAlbum(Integer albumId) {
        return albumService.getObjectById(albumId).getSongs();
    }

    ////////////////////////
    @PostMapping("/{songId}/singer/{singerId}")
    public Collection<Song> changeSongSinger(@PathVariable Integer songId, @PathVariable Integer singerId) {
        Song song = songService.getObjectById(songId);
        removeSongFromSinger(song);
        return addSongToSinger(song, singerId);
    }
    private Collection<Song> addSongToSinger(Song newSong, Integer singerId) {
        Song song = songService.getObjectById(newSong.getId());
        if (singerId == -2)
            return null;
        Singer singer = singerService.getObjectById(singerId);
        song.setSinger(singer);
        singer.addSong(song);
        singer.setSongs(singer.getSongs());
        singerService.saveObject(singer);
        songService.saveObject(song);
        return getSongsOfSinger(singerId);
    }
    private void removeSongFromSinger(Song song) {
        if (song.getSinger() != null) {
            Singer singer = singerService.getObjectById(song.getSinger().getId());
            singer.removeSong(song);
            singer.setSongs(singer.getSongs());
            singerService.saveObject(singer);
            songService.saveObject(song);
        }
    }
    private Collection<Song> getSongsOfSinger(Integer singerId) {
        return singerService.getObjectById(singerId).getSongs();
    }
}

