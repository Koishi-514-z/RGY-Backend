package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    
}
