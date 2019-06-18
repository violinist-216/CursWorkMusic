package com.example.repositories;

import com.example.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProducerRepository extends JpaRepository<Producer, Integer> {
}
