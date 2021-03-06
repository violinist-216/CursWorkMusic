package com.example.repositories;

import com.example.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SongRepository extends JpaRepository<Song, Integer> {
}
