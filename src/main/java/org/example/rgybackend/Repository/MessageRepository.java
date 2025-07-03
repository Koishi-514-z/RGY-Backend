package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySessionid(Long sessionid);

    List<Message> findByFromuserid(String fromuserid);

    List<Message> findByTouserid(String touserid);
}
