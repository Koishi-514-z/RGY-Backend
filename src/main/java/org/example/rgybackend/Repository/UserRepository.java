package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.DTO.ProfileTag;
import org.example.rgybackend.Entity.UserProfile;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserProfile, String> {
    List<UserProfile> findByUsername(String username);

    List<UserProfile> findByRole(Long role);

    boolean existsByUsername(String username);

    @Query("SELECT new org.example.rgybackend.Model.SimplifiedProfileModel(p.userid, p.username, p.avatar, p.note, p.jointime) FROM UserProfile p")
    List<SimplifiedProfileModel> findAllSimplified();

    @Query("SELECT new org.example.rgybackend.DTO.ProfileTag(p.userid, p.username) FROM UserProfile p")
    List<ProfileTag> findAllTags();

    @Query("SELECT new org.example.rgybackend.DTO.ProfileTag(p.userid, p.username) FROM UserProfile p WHERE p.role = 2")
    List<ProfileTag> findPsyTags();

    @Query("SELECT new org.example.rgybackend.Model.SimplifiedProfileModel(p.userid, p.username, p.avatar, p.note, p.jointime) FROM UserProfile p WHERE p.userid = :userid")
    SimplifiedProfileModel findSimplifiedById(String userid);

    @Query("SELECT p.role FROM FROM UserProfile p WHERE p.userid = :userid")
    Long getRole(String userid);
    @Query("SELECT p.username FROM FROM UserProfile p WHERE p.userid = :userid")
    String getUsername(String userid);
}
