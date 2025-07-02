package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    //Optional<Blog> findById(Long blogid);
    Blog findByTitle(String title);
    //返回所有博客
    //List<Blog> findAll();

}
