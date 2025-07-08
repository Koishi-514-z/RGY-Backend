package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByUserid(String userid);
    Blog findByTimestampAndUserid(Long timestamp, String userid);
}
