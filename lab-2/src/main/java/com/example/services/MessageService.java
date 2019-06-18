package com.example.services;

import com.example.Messages.Message;
import com.example.Messages.MessageRepository;
import com.example.Service;
import com.example.exceptions.MessageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class MessageService implements Service<Message, Long> {
    @Autowired
    MessageRepository repository;
    @Override
    public List<Message> getAll() {
        return repository.findAll();
    }

    @Override
    public Message getObjectById(Long aLong) {
        Optional<Message> message = repository.findById(aLong);
        if(message.isPresent()){
            return message.get();
        }
        throw new MessageNotFoundException(aLong);
    }

    @Override
    public Message saveObject(Message newObject) {
        return null;
    }

    @Override
    public void deleteObject(Long aLong) {

    }

    @Override
    public Message updateObject(Message newObject, Long aLong) {
        return null;
    }
}
