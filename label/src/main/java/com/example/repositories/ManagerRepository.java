package com.example.repositories;

import com.example.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
}
