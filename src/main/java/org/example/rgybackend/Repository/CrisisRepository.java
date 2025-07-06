package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Crisis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrisisRepository extends JpaRepository<Crisis, Long> {
    
}
