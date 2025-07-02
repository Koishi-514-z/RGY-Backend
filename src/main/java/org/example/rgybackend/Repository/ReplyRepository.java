package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.DTO.ReplyData;
import org.example.rgybackend.Entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("SELECT new org.example.rgybackend.DTO.ReplyData(r.replyid, r.touserid, r.timestamp) FROM Reply r WHERE r.fromuserid = :fromuserid")
    List<ReplyData> findToUser(String fromuserid);

    @Query("SELECT new org.example.rgybackend.DTO.ReplyData(r.replyid, r.fromuserid, r.timestamp) FROM Reply r WHERE r.touserid = :touserid")
    List<ReplyData> findFromUser(String touserid);
}
