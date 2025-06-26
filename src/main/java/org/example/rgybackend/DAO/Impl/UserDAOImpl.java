package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.Entity.UserProfile;
import org.example.rgybackend.Model.Profile;
import org.example.rgybackend.Model.SimplifiedProfile;
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
    public List<Profile> getAll() {
        List<UserProfile> userProfileDTOs = userRepository.findAll();
        List<Profile> profiles = new ArrayList<>();
        for(UserProfile userProfileDTO : userProfileDTOs) {
            profiles.add(new Profile(userProfileDTO));
        }
        return profiles;
    }

    @Override
    public Profile get(String userid) {
        Optional<UserProfile> profileOptional = userRepository.findById(userid);
        if(profileOptional.isEmpty()) {
            throw new NotExistException("User not exists, userid: " + userid);
        }
        UserProfile userProfileDTO = profileOptional.get();
        Profile profile = new Profile(userProfileDTO);
        return profile;
    }

    @Override
    public Profile getByName(String username) {
        List<UserProfile> userProfileDTOs = userRepository.findByUsername(username);
        if(userProfileDTOs.size() == 0) {
            throw new NotExistException("User not exists, username: " + username);
        }
        if(userProfileDTOs.size() > 1) {
            throw new RuntimeException("Duplicate user, username: " + username);
        }
        UserProfile userProfileDTO = userProfileDTOs.get(0);
        return new Profile(userProfileDTO);
    }

    @Override
    public SimplifiedProfile getSimplified(String userid) {
        return userRepository.findSimplifiedById(userid);
    }

    @Override
    public boolean add(Profile profile) {
        String userid = profile.getUserid();
        if(existed(userid)) {
            throw new RuntimeException("Duplicate user, userid: " + userid);
        }
        UserProfile userProfileDTO = new UserProfile(profile);
        userRepository.save(userProfileDTO);
        return true;
    }

    @Override
    public boolean update(Profile profile) {
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

}
