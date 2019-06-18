package com.example.controllers;

import com.example.Producer;
import com.example.Singer;
import com.example.SingerService;
import com.example.exceptions.ProducerNotFoundException;
import com.example.exceptions.ProducerNotHaveSingersException;
import com.example.services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/producers")
public class ProducerController {

    @Autowired
    private ProducerService producerService;
    @Autowired
    private SingerService singerService;

    @GetMapping
    public Collection<Producer> getProducers() {
        return producerService.getAll();
    }

    @GetMapping(value = "/{producerId}", produces = "application/json; charset=UTF-8")
    public Producer getProducer(@PathVariable Integer producerId) {
        return producerService.getObjectById(producerId);
    }

    @PostMapping
    public Producer createProducer(@Valid @RequestBody Producer newProducer) {
        return producerService.saveObject(newProducer);
    }

    @PutMapping(value = "/{producerId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Producer updateProducer(@Valid @RequestBody Producer updatedProducer, @PathVariable Integer producerId) {
        return producerService.updateObject(updatedProducer, producerId);
    }

    @DeleteMapping("/{producerId}")
    public ResponseEntity<?> deleteProducerAndSaveSingers(@PathVariable Integer producerId) {
        Producer producer = producerService.getObjectById(producerId);
        Singer[] singers = new Singer[producer.getSingers().size()];
        singers = producer.getSingers().toArray(singers);
        for (Singer s : singers) {
            Singer singer = singerService.getObjectById(s.getId());
            singer.setProducer(null);
            producer.removeSinger(singer);
            producer.setSingers(producer.getSingers());
            singerService.saveObject(singer);
            producerService.saveObject(producer);
        }
        return deleteProducerAndSingers(producerId);
    }

    @DeleteMapping("/{producerId}/delall")
    public ResponseEntity<?> deleteProducerAndSingers(@PathVariable Integer producerId) {
        producerService.deleteObject(producerId);
        return ResponseEntity.ok().build();
    }

    ////////////////////////
    @DeleteMapping("/{producerId}/singers/{singerId}")
    public Collection<Singer> removeSingerFromProducer(@PathVariable Integer producerId, @PathVariable Integer singerId) {
        Singer singer = singerService.getObjectById(singerId);
        Producer producer = producerService.getObjectById(producerId);
        if (!producer.getSingers().contains(singer))
            throw new ProducerNotHaveSingersException(producer, singer);
        else {
            singer.setProducer(null);
            producer.removeSinger(singer);
            producer.setSingers(producer.getSingers());
            producerService.saveObject(producer);
            singerService.saveObject(singer);
        }
        return getSingersOfProducer(producerId);
    }
    public Collection<Singer> getSingersOfProducer(@PathVariable Integer producerId) {
        return producerService.getObjectById(producerId).getSingers();
    }
}