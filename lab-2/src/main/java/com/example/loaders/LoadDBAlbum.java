package com.example.loaders;

import com.example.Album;
import com.example.Song;
import com.example.repositories.AlbumRepository;
import com.example.repositories.SongRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.var;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBAlbum {

    @Bean
    public CommandLineRunner initDbAlbum(AlbumRepository repository) {
        return args -> {
            List<Album> newList = repository.findAll();
            if (((List) newList).isEmpty()) {
                System.out.println("Preloading album " + repository.save((new Album("Black", "rap"))));
                System.out.println("Preloading album " + repository.save(new Album("White", "rock")));
            } else {
                System.out.println("Current rows in table albums: ");
                repository.findAll().forEach(album -> System.out.println(album.getId() + ":" + album.getTitle()  + " has genre: " + album.getGenre()));
            }
        };
    }
}
