package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.DTO.ReplyData;
import org.example.rgybackend.Entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("SELECT new org.example.rgybackend.DTO.ReplyData(d.replyid, d.touserid, d.timestamp) FROM Reply r WHERE r.fromuserid = :fromuserid")
    List<ReplyData> findOppositeUser(String fromuserid);
}
