package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.Entity.UserProfile;
import org.example.rgybackend.Model.SimplifiedProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserProfile, String> {
    List<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT new org.example.rgybackend.Entity.SimplifiedProfile(p.userid, p.username, p.avatar, p.note) FROM UserProfileDTO p")
    List<SimplifiedProfile> findAllSimplified();

    @Query("SELECT new org.example.rgybackend.Entity.SimplifiedProfile(p.userid, p.username, p.avatar, p.note) FROM UserProfileDTO p WHERE p.userid = :userid")
    SimplifiedProfile findSimplifiedById(String userid);
}
