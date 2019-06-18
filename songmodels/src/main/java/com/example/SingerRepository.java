package com.example;

import com.example.Singer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SingerRepository extends JpaRepository<Singer, Integer> {
}
