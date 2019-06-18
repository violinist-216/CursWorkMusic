package com.example.loaders;

import com.example.Singer;
import com.example.SingerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@Slf4j
public class LoadDBSinger {
    @Bean
    public CommandLineRunner initSingerDB(SingerRepository repository) {
        return args -> {
            List<Singer> list = repository.findAll();
            if (((List) list).isEmpty()) {
                System.out.println("Preloading singer " + repository.save(new Singer("Vinnik", 42)));
                System.out.println("Preloading singer " + repository.save(new Singer("Sibrov", 67)));
            } else {
                System.out.println("Current rows in table singers: ");
                repository.findAll().forEach(singer -> System.out.println(singer.getId() + ":" + singer.getSingername() + "with age " + singer.getSingerage()));
            }
        };
    }

}
