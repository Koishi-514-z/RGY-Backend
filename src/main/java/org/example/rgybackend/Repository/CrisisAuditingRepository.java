package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.CrisisAuditing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrisisAuditingRepository extends JpaRepository<CrisisAuditing, Integer> {

}
