package com.example.controllers;

import com.example.Album;
import com.example.Singer;
import com.example.Song;
import com.example.exceptions.AlbumNotFoundException;
import com.example.exceptions.AlbumNotHaveSongsException;
import com.example.services.AlbumService;
import com.example.SingerService;
import com.example.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private SingerService singerService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private SongService songService;
    @GetMapping
    public Collection<Album> getAlbums() {
        List<Album> list = albumService.getAll();
        List<Album> list2=  albumService.getAll().stream()
                .collect(Collectors.toList());
        return list;
    }

    @GetMapping(value = "/{albumId}", produces = "application/json; charset=UTF-8")
    public Album getAlbum(@PathVariable Integer albumId) {
        return albumService.getObjectById(albumId);        
    }

    @PostMapping
    public Album createAlbum(@Valid @RequestBody Album newAlbum) {
        return albumService.saveObject(newAlbum);
    }

    @PutMapping(value = "/{albumId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Album updateAlbum(@Valid @RequestBody Album updatedAlbum, @PathVariable Integer albumId) {
        return albumService.updateObject(updatedAlbum, albumId);
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<?> deleteAlbumAndSaveSongs(@PathVariable Integer albumId) {
        Album album = albumService.getObjectById(albumId);
        Song[] songs = new Song[album.getSongs().size()];
        songs = album.getSongs().toArray(songs);
        for (Song stud :
                songs) {
            Song song = songService.getObjectById(stud.getId());
            song.setAlbum(null);
            album.removeSong(song);
            album.setSongs(album.getSongs());
            songService.saveObject(song);
            albumService.saveObject(album);
        }

        try {
            changeAlbumSinger(albumId, -2);
            albumService.deleteObject(albumId);
        } catch (AlbumNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }
        
        return deleteAlbumAndSongs(albumId);
    }

    @DeleteMapping("/{albumId}/delall")
    public ResponseEntity<?> deleteAlbumAndSongs(@PathVariable Integer albumId) {
        albumService.deleteObject(albumId);
        return ResponseEntity.ok().build();
    }
    ///////////////////////////////////

    @DeleteMapping("/{albumId}/songs/{songId}")
    public List<Song> removeSongFromAlbum(@PathVariable Integer albumId, @PathVariable Integer songId) {
        Song song = songService.getObjectById(songId);
        Album album = albumService.getObjectById(albumId);
        if (!album.getSongs().contains(song))
            throw new AlbumNotHaveSongsException(album, song);
        else {
            song.setAlbum(null);
            album.removeSong(song);
            album.setSongs(album.getSongs());
            albumService.saveObject(album);
            songService.saveObject(song);
        }
        return getSongsOfAlbum(albumId);
    }
    public List<Song> getSongsOfAlbum(@PathVariable Integer albumId) {
        return albumService.getObjectById(albumId).getSongs();
    }

    //////////////////////////////////
    @PostMapping("/{albumId}/singer/{newSingerId}")
    public List<Album> changeAlbumSinger(@PathVariable Integer albumId, @PathVariable Integer newSingerId) {
        Album album = albumService.getObjectById(albumId);
        removeAlbumFromSinger(album);
        return addAlbumToSinger(album, newSingerId);
    }
    private void removeAlbumFromSinger(Album album) {
        if (album.getSinger() != null) {
            Singer albumSinger = singerService.getObjectById(album.getSinger().getId());
            albumSinger.removeAlbum(album);
            albumSinger.setAlbums(albumSinger.getAlbums());
            singerService.saveObject(albumSinger);
            albumService.saveObject(album);
        }
    }
    private List<Album> addAlbumToSinger(Album album, Integer singerId) {
        Album album1 = albumService.getObjectById(album.getId());
        if (singerId == -2)
            return null;
        Singer singer = singerService.getObjectById(singerId);
        album1.setSinger(singer);
        singer.addAlbum(album1);
        singer.setAlbums(singer.getAlbums());
        singerService.saveObject(singer);
        albumService.saveObject(album1);
        return getAlbumsOfSinger(singerId);
    }
    private List<Album> getAlbumsOfSinger(Integer singerId) {
        return singerService.getObjectById(singerId).getAlbums();
    }
}