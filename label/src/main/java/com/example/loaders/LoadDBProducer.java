package com.example.loaders;

import com.example.Male;
import com.example.Producer;
import com.example.repositories.ManagerRepository;
import com.example.repositories.ProducerRepository;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBProducer {
    @Bean
    public CommandLineRunner initProducerDB(ProducerRepository repository) {
        return args -> {
            List<Producer> list =  repository.findAll();
            if (list.isEmpty()) {
                System.out.println("Preloading producer  " + repository.save(new Producer("Koliya",  22,4, Male.MALE)));
                System.out.println("Preloading producer  " + repository.save(new Producer("Masha",  32, 8,  Male.FEMALE)));
            } else {
                System.out.println("Current rows in table producers: ");
                repository.findAll().forEach(producer -> System.out.println(producer.getId() + ":" + producer.getFullName() + "with age " + producer.getAge() + "has male" + producer.getMale() + "have experience" + producer.getExperience()));
            }
        };
    }

}
