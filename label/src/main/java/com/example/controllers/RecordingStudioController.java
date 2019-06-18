package com.example.controllers;

import com.example.RecordingStudio;
import com.example.services.RecordingStudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/recordingStudies")
public class RecordingStudioController {

    @Autowired
    private RecordingStudioService recordingStudioService;
    @GetMapping
    public Collection<RecordingStudio> getRecordingStudios() {
        return recordingStudioService.getAll();
    }

    @GetMapping("/{recordingStudioId}")
    public RecordingStudio getRecordingStudio(@PathVariable Integer recordingStudioId) {
        return recordingStudioService.getObjectById(recordingStudioId);
    }

    @PostMapping
    public RecordingStudio createRecordingStudio(@Valid @RequestBody RecordingStudio newRecordingStudio) {
        return recordingStudioService.saveObject(newRecordingStudio);
    }
    @PutMapping("/{recordingStudioId}")
    public RecordingStudio updateRecordingStudio(@Valid @RequestBody RecordingStudio updatedRecordingStudio, @PathVariable Integer recordingStudioId) {
        return recordingStudioService.updateObject(updatedRecordingStudio, recordingStudioId);
    }

    @DeleteMapping("/{recordingStudioId}")
    public ResponseEntity<?> deleteManagerAndSaveRecordingStudios(@PathVariable Integer recordingStudioId) {
        recordingStudioService.deleteObject(recordingStudioId);
        return ResponseEntity.ok().build();
    }
}

