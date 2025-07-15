package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.DTO.IntimateDTO;
import org.example.rgybackend.DTO.ProfileTag;
import org.example.rgybackend.Model.*;

public interface UserService {
    boolean isAdmin(String userid);

    boolean verifyPasswordByName(String username, String password);

    boolean userExisted(String username);

    ProfileModel getUserProfile(String userid);

    List<AdminProfileModel> getAllProfile(String adminid);

    PsyProfileModel getPsyProfile(String psyid);

    List<PsyProfileModel> getPsyProfiles();

    ProfileModel getProfileByName(String username);

    SimplifiedProfileModel getSimplifiedProfile(String userid);

    List<IntimateDTO> getIntimateUsers(String userid);

    boolean isDisabled(String userid);

    boolean verifyPassword(String userid, String password);

    boolean verifyAdmin(String verifyKey);

    boolean addUser(UserModel user);

    boolean updateProfile(ProfileModel profile);

    boolean updatePsyProfile(PsyProfileModel psyProfileModel);

    boolean updatePassword(String userid, String password);

    boolean setDisabled(String userid, boolean disabled);

    ProfileTag getProfileTag(String userid);
  
    boolean postAuthCode(String email);

    boolean checkAuthCode(String email, Long authCode);
}
