package com.example.services;

import com.example.RecordingStudio;
import com.example.exceptions.RecordingStudioNotFoundException;
import com.example.repositories.RecordingStudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyRecordingStudioService implements RecordingStudioService {

    @Autowired
    private RecordingStudioRepository recordingStudioRepository;

    @Override
    public List<RecordingStudio> getAll() {
        return recordingStudioRepository.findAll()
                .stream()
                .filter(recordingStudio -> !recordingStudio.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public RecordingStudio getObjectById(Integer integer) {
        Optional<RecordingStudio> ns = recordingStudioRepository.findById(integer);
        if(ns.isPresent())
            if (!ns.get().getDeleted())
            return ns.get();

        throw new RecordingStudioNotFoundException(integer);
    }

    @Override
    public RecordingStudio saveObject(RecordingStudio newObject) {
        return recordingStudioRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        RecordingStudio os = getObjectById(integer);
        os.delete();
        recordingStudioRepository.save(os);
    }

    @Override
    public RecordingStudio updateObject(RecordingStudio newObject, Integer integer) {
        RecordingStudio foundRecordingStudio = getObjectById(integer);
        foundRecordingStudio.setName(newObject.getName());
        foundRecordingStudio.setAddress(newObject.getAddress());
        foundRecordingStudio.setRent(newObject.getRent());
        return recordingStudioRepository.save(foundRecordingStudio);
    }
}
