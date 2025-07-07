package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.NotificationPrivate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface NotificationPrivateRepository extends JpaRepository<NotificationPrivate, Long> {
    List<NotificationPrivate> findByUserid(String userid);
}
