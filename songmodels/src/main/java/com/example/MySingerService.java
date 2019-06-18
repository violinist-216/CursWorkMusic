package com.example;

import com.example.exceptions.SingerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MySingerService implements SingerService {

    @Autowired
    private SingerRepository singerRepository;

    @Override
    public List<Singer> getAll() {
        return singerRepository.findAll()
                .stream()
                .filter(singer -> !singer.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Singer getObjectById(Integer integer)
    {
        Optional<Singer> ns = singerRepository.findById(integer);
        if(ns.isPresent()){
            if(!ns.get().getDeleted())
                return ns.get();
        }
        throw new SingerNotFoundException(integer);
    }

    @Override
    public Singer saveObject(Singer newObject) {
        return singerRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Singer os = getObjectById(integer);
        os.delete();
        singerRepository.save(os);
    }

    @Override
    public Singer updateObject(Singer newObject, Integer integer) {
        Singer foundSinger = getObjectById(integer);
        foundSinger.setSingername(newObject.getSingername());
        foundSinger.setSingerage(newObject.getSingerage());
        /*foundSinger.setProducer(newObject.getProducer());
        foundSinger.setSongs(newObject.getSongs());
        foundSinger.setAlbums(newObject.getAlbums());*/
        return singerRepository.save(foundSinger);
    }
}
