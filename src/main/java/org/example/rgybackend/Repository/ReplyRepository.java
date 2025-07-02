package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBlogid(Long blogid);

}
