package com.example.services;

import com.example.Producer;
import com.example.exceptions.ProducerNotFoundException;
import com.example.repositories.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyProducerService implements ProducerService {

    @Autowired
    private ProducerRepository producerRepository;

    @Override
    public List<Producer> getAll() {
        return producerRepository.findAll()
                .stream()
                .filter(producer -> !producer.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Producer getObjectById(Integer integer)
    {
        Optional<Producer> ns = producerRepository.findById(integer);
        if(ns.isPresent()){
            if(!ns.get().getDeleted())
                return ns.get();
        }
        throw new ProducerNotFoundException(integer);
    }

    @Override
    public Producer saveObject(Producer newObject) {
        return producerRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Producer os = getObjectById(integer);
        os.delete();
        producerRepository.save(os);
    }

    @Override
    public Producer updateObject(Producer newObject, Integer integer) {

        Producer foundProducer = getObjectById(integer);
        foundProducer.setFullName(newObject.getFullName());
        foundProducer.setAge(newObject.getAge());
        foundProducer.setMale(newObject.getMale());
        foundProducer.setExperience(newObject.getExperience());
        /*foundProducer.setSingers(newObject.getSingers());*/
        return producerRepository.save(foundProducer);
    }
}
