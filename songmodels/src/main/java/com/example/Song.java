package com.example;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "songs")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "deleted", allowGetters = true)
public class Song extends com.example.Entity {

    @NotBlank(message = "Task title must not be blank!")
    private String title;

    @NotBlank(message = "Task content must not be blank!")
    private String content;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "album_id")
    private Album album;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "singer_id")
    private Singer singer;

    public Song() {
    }

    public Song(@NotBlank String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Song(@NotBlank String title, @NotBlank String content, Album album, Singer singer) {
        this.title = title;
        this.content = content;
        this.album = album;
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Album getAlbum() {
        return album;
    }

    public Singer getSinger() {return singer;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setSinger(Singer singer) {this.singer = singer;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (!title.equals(song.title)) return false;
        return content.equals(song.content);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }
}
