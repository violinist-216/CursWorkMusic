package com.example.services;

import com.example.Album;
import com.example.exceptions.AlbumNotFoundException;
import com.example.exceptions.SingerNotFoundException;
import com.example.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyAlbumService implements AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public List<Album> getAll() {
        return albumRepository.findAll()
                .stream()
                .filter(album -> !album.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Album getObjectById(Integer integer) {
        Optional<Album> ns = albumRepository.findById(integer);
        if(ns.isPresent())
            if (!ns.get().getDeleted())
                return ns.get();

        throw new AlbumNotFoundException(integer);
    }

    @Override
    public Album saveObject(Album newObject) {
        return albumRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Album os = getObjectById(integer);
        os.delete();
        albumRepository.save(os);
    }

    @Override
    public Album updateObject(Album newObject, Integer integer) {
        Album foundAlbum = getObjectById(integer);
        foundAlbum.setTitle(newObject.getTitle());
        foundAlbum.setGenre(newObject.getGenre());
        /*foundAlbum.setSinger(newObject.getSinger());
        foundAlbum.setSongs(newObject.getSongs());*/
        return albumRepository.save(foundAlbum);
    }
}
