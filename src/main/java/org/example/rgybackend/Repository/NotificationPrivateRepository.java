package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.NotificationPrivate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NotificationPrivateRepository extends JpaRepository<NotificationPrivate, Long> {
    List<NotificationPrivate> findByUserid(String userid);

    @Query("SELECT n FROM NotificationPrivate n WHERE n.type < 1000")
    List<NotificationPrivate> findPrivateByUserid(String userid);

    @Query("SELECT n FROM NotificationPrivate n WHERE n.type >= 1000")
    List<NotificationPrivate> findPublicByUserid(String userid);
}
