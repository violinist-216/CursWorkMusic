package com.example.repositories;

import com.example.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AlbumRepository extends JpaRepository<Album, Integer> {
}
