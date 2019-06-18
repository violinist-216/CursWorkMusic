package com.example.controllers;

import com.example.Producer;
import com.example.Singer;
import com.example.Album;
import com.example.Song;
import com.example.exceptions.*;
import com.example.services.ProducerService;
import com.example.SingerService;
import com.example.services.AlbumService;
import com.example.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/singers")
public class SingerController {

    @Autowired
    private SingerService singerService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private SongService songService;
    @Autowired
    private ProducerService producerService;

    @GetMapping
    public Collection<Singer> getSingers() {
        return singerService.getAll();
    }

    @GetMapping(value = "/{singerId}", produces = "application/json; charset=UTF-8")
    public Singer getSinger(@PathVariable Integer singerId) {
        return singerService.getObjectById(singerId);
    }

    @PostMapping
    public Singer createSinger(@Valid @RequestBody Singer newSinger)  {
        return singerService.saveObject(newSinger);
    }

    @PutMapping(value = "/{singerId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Singer updateSinger(@Valid @RequestBody Singer updatedSinger, @PathVariable Integer singerId)  {
        return singerService.updateObject(updatedSinger, singerId);
    }

    //////////////////////////
    @DeleteMapping("/{singerId}")
    public ResponseEntity<?> deleteSingerAndSaveAll(@PathVariable int singerId){
        Singer singer = singerService.getObjectById(singerId);
        Song[] songs = new Song[singer.getSongs().size()];
        songs = singer.getSongs().toArray(songs);
        for (Song stud : songs) {
            Song song = songService.getObjectById(stud.getId());
            song.setSinger(null);
            singer.removeSong(song);
            singer.setSongs(singer.getSongs());
            songService.saveObject(song);
            singerService.saveObject(singer);
        }

        Album[] albums = new Album[singer.getAlbums().size()];
        albums = singer.getAlbums().toArray(albums);
        for (Album stud : albums) {
            Album album = albumService.getObjectById(stud.getId());
            album.setSinger(null);
            singer.removeAlbum(album);
            singer.setAlbums(singer.getAlbums());
            albumService.saveObject(album);
            singerService.saveObject(singer);
        }
        
        return deleteSingerAndAlbumsAndSongs(singerId);
    }
    @DeleteMapping("/{singerId}/delall")
    public ResponseEntity<?> deleteSingerAndAlbumsAndSongs(@PathVariable Integer singerId) {
        try {
            singerService.deleteObject(singerId);
        } catch (SingerNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    ///////////////////////////////////////
    /* ALBUM */
    @DeleteMapping("/{singerId}/albums/{albumId}")
    public Collection<Album> removeAlbumFromSinger(@PathVariable Integer singerId, @PathVariable Integer albumId) {
        Album album = albumService.getObjectById(albumId);
        Singer singer = singerService.getObjectById(singerId);
        if (!singer.getAlbums().contains(album))
            throw new SingerNotHaveAlbumsException(singer, album);
        else {
            album.setSinger(null);
            singer.removeAlbum(album);
            singer.setAlbums(singer.getAlbums());
            singerService.saveObject(singer);
            albumService.saveObject(album);
        }
        return getAlbumsOfSinger(singerId);
    }
    public Collection<Album> getAlbumsOfSinger(@PathVariable Integer singerId) {
        return singerService.getObjectById(singerId).getAlbums();
    }
    /* SONG */
    @DeleteMapping("/{singerId}/songs/{songId}")
    public List<Song> removeSongFromSinger(@PathVariable Integer singerId, @PathVariable Integer songId) {
        Song song = songService.getObjectById(songId);
        Singer singer = singerService.getObjectById(singerId);
        if (!singer.getSongs().contains(song))
            throw new SingerNotHaveSongException(singer, song);
        else {
            song.setSinger(null);
            singer.removeSong(song);
            singer.setSongs(singer.getSongs());
            singerService.saveObject(singer);
            songService.saveObject(song);
        }
        return getSongsOfSinger(singerId);
    }
    public List<Song> getSongsOfSinger(@PathVariable Integer singerId) {
        return singerService.getObjectById(singerId).getSongs();
    }

    /* PRODUCER */
    @PostMapping("/{singerId}/producer/{newProducerId}")
    public List<Singer> changeSingerProducer(@PathVariable Integer singerId, @PathVariable Integer newProducerId) {
        Singer singer = singerService.getObjectById(singerId);
        removeSingerFromProducer(singer);
        return addSingerToProducer(singer, newProducerId);
    }
    private void removeSingerFromProducer(Singer singer) {
        if (singer.getProducer() != null) {
            Producer singerProducer = producerService.getObjectById(singer.getProducer().getId());
            singerProducer.removeSinger(singer);
            singerProducer.setSingers(singerProducer.getSingers());
            producerService.saveObject(singerProducer);
            singerService.saveObject(singer);
        }
    }
    private List<Singer> addSingerToProducer(Singer singer, Integer producerId) {
        Singer stud = singerService.getObjectById(singer.getId());

        if (producerId == -2)
            return null;
        Producer producer = producerService.getObjectById(producerId);
        stud.setProducer(producer);
        producer.addSinger(stud);
        producer.setSingers(producer.getSingers());
        producerService.saveObject(producer);
        singerService.saveObject(stud);
        return getSingersOfProducer(producerId);
    }
    private List<Singer> getSingersOfProducer(Integer producerId) {
        return producerService.getObjectById(producerId).getSingers();
    }
}