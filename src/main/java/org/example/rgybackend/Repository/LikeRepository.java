package org.example.rgybackend.Repository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.example.rgybackend.DTO.LikeData;
import org.example.rgybackend.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT new org.example.rgybackend.DTO.LikeData(l.likeid, l.touserid, l.timestamp) FROM Like l WHERE l.fromuserid = :fromuserid")
    List<LikeData> findToUser(String fromuserid);

    @Query("SELECT new org.example.rgybackend.DTO.LikeData(l.likeid, l.fromuserid, l.timestamp) FROM Like l WHERE l.touserid = :touserid")
    List<LikeData> findFromUser(String touserid);
    
    @Modifying
    @Transactional
    void deleteByFromuseridAndBlogid(String fromuserid, Long blogid);

    List<Like> findAllByFromuserid(String fromuserid);
}
