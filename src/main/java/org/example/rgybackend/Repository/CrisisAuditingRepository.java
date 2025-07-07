package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.CrisisAuditing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrisisAuditingRepository extends JpaRepository<CrisisAuditing, Long> {
    
}
