package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<UserAuth, String> {
    
}
