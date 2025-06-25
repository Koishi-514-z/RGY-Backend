package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Entity.Profile;
import org.example.rgybackend.Entity.SimplifiedProfile;
import org.example.rgybackend.Entity.User;

public interface UserService {
    boolean verifyPasswordByName(String username, String password);

    boolean userExisted(String username);

    Profile getUserProfile(String userid);

    Profile getProfileByName(String username);

    SimplifiedProfile getSimplifiedProfile(String userid);

    List<SimplifiedProfile> getIntimateUsers(String userid);

    boolean verifyPassword(String userid, String password);

    boolean addUser(User user);

    boolean updateProfile(Profile profile);

    boolean updatePassword(String userid, String password);
}
