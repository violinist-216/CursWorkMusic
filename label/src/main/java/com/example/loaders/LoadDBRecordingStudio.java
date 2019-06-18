package com.example.loaders;

import com.example.RecordingStudio;
import com.example.repositories.ProducerRepository;
import com.example.repositories.RecordingStudioRepository;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBRecordingStudio {

    @Bean
    public CommandLineRunner initDbRecordingStudio(RecordingStudioRepository repository) {
        return args -> {
            List<RecordingStudio> checkList =  repository.findAll();
            if (((List) checkList).isEmpty()) {
                System.out.println("Preloading recordingStudio " + repository.save((new RecordingStudio("12", "3", 123))));
                System.out.println("Preloading recordingStudio " + repository.save(new RecordingStudio("qwer", "999", 125)));
            } else {
                System.out.println("Current rows in table recordingStudious: ");
                repository.findAll().forEach(recordingStudio -> System.out.println(recordingStudio.getId() + ":" + recordingStudio.getName() + "with address " + recordingStudio.getAddress() + "cost for a day: " + recordingStudio.getRent()));
            }
        };
    }
}
