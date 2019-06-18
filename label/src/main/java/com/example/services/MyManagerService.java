package com.example.services;

import com.example.Manager;
import com.example.exceptions.RecordingStudioNotFoundException;
import com.example.exceptions.ProducerNotFoundException;
import com.example.repositories.ManagerRepository;
import com.example.repositories.RecordingStudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyManagerService implements ManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<Manager> getAll() {
        return managerRepository.findAll()
                .stream()
                .filter(manager -> !manager.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Manager getObjectById(Integer integer) {
        Optional <Manager> ns = managerRepository.findById(integer);
        if(ns.isPresent())
            if (!ns.get().getDeleted())
                return ns.get();

        throw new ProducerNotFoundException(integer);

    }

    @Override
    public Manager saveObject(Manager newObject) {
        return managerRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Manager os = getObjectById(integer);
        os.delete();
        managerRepository.save(os);
    }

    @Override
    public Manager updateObject(Manager newObject, Integer integer) {
        Manager foundManager = getObjectById(integer);
        foundManager.setFullName(newObject.getFullName());
        foundManager.setAge(newObject.getAge());
        foundManager.setMale(newObject.getMale());
        foundManager.setManagerType(newObject.getManagerType());
        return managerRepository.save(foundManager);

    }
}
