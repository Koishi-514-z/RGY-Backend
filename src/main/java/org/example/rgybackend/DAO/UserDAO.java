package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.DTO.ProfileTag;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;

public interface UserDAO {
    boolean existed(String userid);

    boolean existedByName(String username);

    ProfileModel get(String userid);

    ProfileModel getByName(String username);

    SimplifiedProfileModel getSimplified(String userid);

    boolean add(ProfileModel profile);

    boolean update(ProfileModel profile);

    Long getRole(String userid);

    List<ProfileTag> getAllProfileTags();

    List<ProfileTag> getPsyProfileTags();

    String getUsername(String userid);
}
