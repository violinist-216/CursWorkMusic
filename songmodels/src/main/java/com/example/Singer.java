package com.example;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "singers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "deleted", allowGetters = true)
public class Singer extends com.example.Entity {

    @NotBlank(message = "Task singername must not be blank!")
    private String singername;

    @NotNull(message = "Task singerage must not be null!")
    private Integer singerage;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Album> albums;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Song> songs;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "producer_id")
    private Producer producer;


    public Singer() {}

    public Singer(String singername, Integer singerage) {
        this.singerage = singerage;
        this.singername = singername;
    }

    public Singer(String singername, Integer singerage, Album album, Producer producer, Song song) {
        this.singerage = singerage;
        this.singername = singername;
        setSong(song);
        setAlbum(album);
    this.producer = producer;
    }


    public List<Album> addAlbum(Album album) {
        if (albums.contains(album))
            throw new RuntimeException("This singer already has this album!");
        albums.add(album);
        return albums;
    }

    public List<Song> addSong(Song song) {
        if (songs.contains(song))
            throw new RuntimeException("This singer already has this song!");
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

    public boolean removeAlbum(Album album) {
        return albums.contains(album) && albums.remove(album);
    }

    public boolean removeAlbum(Integer id) {
        for (int i = 0; i < albums.size(); ++i) {
            if (albums.get(i).getId() == id)
                return albums.remove(albums.get(i));
        }
        return false;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public String getSingername() {
        return singername;
    }

    public Integer getSingerage() {
        return singerage;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Song> getSongs() { return songs; }


    public void setSingername(String singername) {
        this.singername = singername;
    }

    public void setSingerage(Integer singerage) {
        this.singerage = singerage;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void setSongs(List<Song> songs) { this.songs = songs; }

    public void  setAlbum(Album album)    {
        List<Album> albums = getAlbums();
        albums.add(album);
    }

    public Album getAlbum (Integer albumId) {
        for (Album s :
                albums) {
            if (s.getId() == albumId)
                return s;
        }
        return getAlbum(albumId);
    }

    public void  setSong(Song song)    {
        List<Song> songs = getSongs();
        songs.add(song);
    }

    public Song getSong (Integer songId) {
        for (Song s :
                songs) {
            if (s.getId() == songId)
                return s;
        }
        return getSong(songId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Singer singer = (Singer) o;

        if (!singername.equals(singer.singername)) return false;
        return singerage.equals(singer.singerage);
    }

    @Override
    public int hashCode() {
        int result = singername.hashCode();
        result = 31 * result + singerage.hashCode();
        return result;
    }
}

