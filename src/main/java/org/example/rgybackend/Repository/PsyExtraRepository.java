package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.PsyProfileExtra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PsyExtraRepository extends JpaRepository<PsyProfileExtra, String> {
    
}
