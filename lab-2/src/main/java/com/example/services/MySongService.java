package com.example.services;

import com.example.Song;
import com.example.exceptions.SingerNotFoundException;
import com.example.exceptions.SongNotFoundException;
import com.example.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MySongService implements SongService {

    @Autowired
    private SongRepository songRepository;

    @Override
    public List<Song> getAll() {
        return songRepository.findAll()
                .stream()
                .filter(song -> !song.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Song getObjectById(Integer integer) {
        Optional<Song> ns = songRepository.findById(integer);
        if(ns.isPresent())
            if (!ns.get().getDeleted())
            return ns.get();

        throw new SongNotFoundException(integer);
    }

    @Override
    public Song saveObject(Song newObject) {
        return songRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Song os = getObjectById(integer);
        os.delete();
        songRepository.save(os);
    }

    @Override
    public Song updateObject(Song newObject, Integer integer) {
        Song foundSong = getObjectById(integer);
        foundSong.setTitle(newObject.getTitle());
        foundSong.setContent(newObject.getContent());
        /*foundSong.setSinger(newObject.getSinger());
        foundSong.setAlbum(newObject.getAlbum());*/
        return songRepository.save(foundSong);
    }
}
