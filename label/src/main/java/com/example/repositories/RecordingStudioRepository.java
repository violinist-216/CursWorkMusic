package com.example.repositories;

import com.example.RecordingStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RecordingStudioRepository extends JpaRepository<RecordingStudio, Integer> {
}
