package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.DTO.ProfileTag;
import org.example.rgybackend.Entity.UserProfile;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Repository.UserRepository;
import org.example.rgybackend.Utils.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean existed(String userid) {
        return userRepository.existsById(userid);
    }

    @Override
    public boolean existedByName(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<ProfileModel> getAll() {
        List<UserProfile> userProfileDTOs = userRepository.findAll();
        List<ProfileModel> profiles = new ArrayList<>();
        for(UserProfile userProfileDTO : userProfileDTOs) {
            profiles.add(new ProfileModel(userProfileDTO));
        }
        return profiles;
    }

    @Override
    public List<ProfileModel> getAllPsys() {
        List<UserProfile> userProfiles = userRepository.findByRole(2L);
        List<ProfileModel> profiles = new ArrayList<>();
        for(UserProfile userProfile : userProfiles) {
            profiles.add(new ProfileModel(userProfile));
        }
        return profiles;
    }

    @Override
    public ProfileModel get(String userid) {
        Optional<UserProfile> profileOptional = userRepository.findById(userid);
        if(profileOptional.isEmpty()) {
            throw new NotExistException("User not exists, userid: " + userid);
        }
        UserProfile userProfileDTO = profileOptional.get();
        ProfileModel profile = new ProfileModel(userProfileDTO);
        return profile;
    }

    @Override
    public ProfileModel getByName(String username) {
        List<UserProfile> userProfileDTOs = userRepository.findByUsername(username);
        if(userProfileDTOs.size() == 0) {
            throw new NotExistException("User not exists, username: " + username);
        }
        if(userProfileDTOs.size() > 1) {
            throw new RuntimeException("Duplicate user, username: " + username);
        }
        UserProfile userProfileDTO = userProfileDTOs.get(0);
        return new ProfileModel(userProfileDTO);
    }

    @Override
    public SimplifiedProfileModel getSimplified(String userid) {
        return userRepository.findSimplifiedById(userid);
    }

    @Override
    public boolean add(ProfileModel profile) {
        String userid = profile.getUserid();
        if(existed(userid)) {
            throw new RuntimeException("Duplicate user, userid: " + userid);
        }
        UserProfile userProfileDTO = new UserProfile(profile);
        userRepository.save(userProfileDTO);
        return true;
    }

    @Override
    public boolean update(ProfileModel profile) {
        String userid = profile.getUserid();
        if(!existed(userid)) {
            throw new NotExistException("User not exists, userid: " + userid);
        }
        UserProfile userProfileDTO = new UserProfile(profile);
        userRepository.save(userProfileDTO);
        return true;
    }

    @Override
    public boolean remove(String userid) {
        userRepository.deleteById(userid);
        return true;
    }

    @Override
    public Long getRole(String userid) {
        return userRepository.getRole(userid);
    }

    @Override
    public List<ProfileTag> getAllProfileTags() {
        return userRepository.findAllTags();
    }

    @Override
    public List<ProfileTag> getPsyProfileTags() {
        return userRepository.findPsyTags();
    }

    @Override
    public String getUsername(String userid) {
        return userRepository.getUsername(userid);
    }

}
