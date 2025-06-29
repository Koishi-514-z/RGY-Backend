package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Model.UserModel;

public interface UserService {
    boolean isAdmin(String userid);

    boolean verifyPasswordByName(String username, String password);

    boolean userExisted(String username);

    ProfileModel getUserProfile(String userid);

    ProfileModel getProfileByName(String username);

    SimplifiedProfileModel getSimplifiedProfile(String userid);

    List<SimplifiedProfileModel> getIntimateUsers(String userid);

    boolean verifyPassword(String userid, String password);

    boolean verifyAdmin(String verifyKey);

    boolean addUser(UserModel user);

    boolean updateProfile(ProfileModel profile);

    boolean updatePassword(String userid, String password);
}
