package com.example.loaders;

import com.example.Manager;
import com.example.repositories.ManagerRepository;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.example.Male.FEMALE;
import static com.example.Male.MALE;
import static com.example.ManagerTypes.BEST_FRIEND;

@Configuration
@Slf4j
public class LoadDBManager {

    @Bean
    public CommandLineRunner initDbManager(ManagerRepository repository) {
        return args -> {
            List<Manager> checkList = repository.findAll();
            if (((List) checkList).isEmpty()) {
                System.out.println("Preloading manager " + repository.save((new Manager("Masha", 24 , FEMALE,BEST_FRIEND))));
                System.out.println("Preloading manager " + repository.save(new Manager("Sasha", 32 , MALE, BEST_FRIEND)));
            } else {
                System.out.println("Current rows in table albums: ");
                repository.findAll().forEach(manager -> System.out.println(manager.getId() + ":" + manager.getFullName() + "with age " + manager.getAge() + "has status: " + manager.getManagerType()));
            }
        };
    }
}
