package com.example.Messages;

import com.example.Messages.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, Long> {
}
