package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.DTO.SessionTagDTO;
import org.example.rgybackend.Entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("SELECT s FROM Session s WHERE (s.userAid = :fromuserid AND s.userBid = :touserid) OR (s.userBid = :fromuserid AND s.userAid = :touserid)")
    List<Session> findSession(String fromuserid, String touserid);

    boolean existsByUserAidAndUserBid(String userAid, String userBid);

    @Query("SELECT new org.example.rgybackend.DTO.SessionTagDTO(s.sessionid, s.userAid, s.userBid, s.timestamp, s.unreadA, s.unreadB) FROM Session s WHERE s.userAid = :fromuserid OR s.userBid = :fromuserid")
    List<SessionTagDTO> findSessionTags(String fromuserid);
}
