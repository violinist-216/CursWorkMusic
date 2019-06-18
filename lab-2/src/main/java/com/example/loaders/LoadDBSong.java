package com.example.loaders;

import com.example.Song;
import com.example.repositories.SongRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.var;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBSong {

    @Bean
    public CommandLineRunner initDbSong(SongRepository repository) {
        return args -> {
            List<Song> checkList = repository.findAll();
            if (((List) checkList).isEmpty()) {
                System.out.println("Preloading song " + repository.save((new Song("Вовчиця", "123456"))));
                System.out.println("Preloading song " + repository.save(new Song("Тралала", "999")));
            } else {
                System.out.println("Current rows in table songs: ");
                repository.findAll().forEach(song -> System.out.println(song.getId() + ":" + song.getTitle() + " has text: " + song.getContent()));
            }
        };
    }
}
