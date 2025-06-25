package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.DTO.UserAuthDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuthDTO, String> {
    
}
