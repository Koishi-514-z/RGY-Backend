package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.Entity.UserProfile;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserProfile, String> {
    List<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT new org.example.rgybackend.Model.SimplifiedProfileModel(p.userid, p.username, p.avatar, p.note) FROM UserProfile p")
    List<SimplifiedProfileModel> findAllSimplified();

    @Query("SELECT new org.example.rgybackend.Model.SimplifiedProfileModel(p.userid, p.username, p.avatar, p.note) FROM UserProfile p WHERE p.userid = :userid")
    SimplifiedProfileModel findSimplifiedById(String userid);
}
