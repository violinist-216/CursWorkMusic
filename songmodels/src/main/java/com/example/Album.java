package com.example;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "albums")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Album extends com.example.Entity {

    @NotBlank(message = "Task title must not be blank!")
    private String title;

    @NotBlank(message = "Task genre must not be blank!")
    private String genre;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "singer_id")
    private Singer singer;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Song> songs;

    public List<Song> addSong(Song song) {
        if (songs.contains(song))
            throw new RuntimeException("Song already in this album!");
        songs.add(song);
        return songs;
    }

    public boolean removeSong(Song song) {
        return songs.contains(song) && songs.remove(song);
    }

    public boolean removeSong(Integer id) {
        for (int i = 0; i < songs.size(); ++i) {
            if (songs.get(i).getId() == id)
                return songs.remove(songs.get(i));
        }
        return false;
    }

    public Album() {
    }

    public Album(@NotBlank String title, @NotBlank String genre) {
        this.title = title;
        this.genre = genre;
    }

    public Album(@NotBlank String title, @NotBlank String genre, @NotNull Singer singer) {
        this.title = title;
        this.genre = genre;
        this.singer = singer;
    }


    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public  Singer getSinger() {
        return singer;
    }

    public List<Song> getSongs() {return songs;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public void setSongs(List<Song> songs) {this.songs = songs;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return title.equals(album.title) &&
                genre.equals(album.genre);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + genre.hashCode();
        return result;
    }
}
