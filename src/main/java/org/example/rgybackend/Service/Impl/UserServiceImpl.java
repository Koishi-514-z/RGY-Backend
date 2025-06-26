package org.example.rgybackend.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.UserAuthDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Model.UserModel;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserAuthDAO userAuthDAO;

    @Override
    public boolean verifyPasswordByName(String username, String password) {
        boolean exists = userDAO.existedByName(username);
        if(!exists) {
            return false;
        }
        ProfileModel profile = userDAO.getByName(username);
        String userid = profile.getUserid();
        return userAuthDAO.pwdMatch(userid, password);
    }

    @Override
    public boolean userExisted(String username) {
        return userDAO.existedByName(username);
    }

    @Override
    public ProfileModel getUserProfile(String userid) {
        return userDAO.get(userid);
    }

    @Override
    public ProfileModel getProfileByName(String username) {
        return userDAO.getByName(username);
    }

    @Override
    public SimplifiedProfileModel getSimplifiedProfile(String userid) {
        return userDAO.getSimplified(userid);
    }

    @Override
    public List<SimplifiedProfileModel> getIntimateUsers(String userid) {
        List<SimplifiedProfileModel> simplifiedProfiles = new ArrayList<>();
        simplifiedProfiles.add(new SimplifiedProfileModel("123456789", "Koishi_plus", null, null));
        simplifiedProfiles.add(new SimplifiedProfileModel("123456789", "Koishi_plus", null, null));
        simplifiedProfiles.add(new SimplifiedProfileModel("123456789", "Koishi_plus", null, null));
        simplifiedProfiles.add(new SimplifiedProfileModel("123456789", "Koishi_plus", null, null));
        return simplifiedProfiles;
    }

    @Override
    public boolean verifyPassword(String userid, String password) {
        return userAuthDAO.pwdMatch(userid, password);
    }

    @Override
    public boolean addUser(UserModel user) {
        boolean result = true;
        result = result && userDAO.add(user.getProfile());
        result = result && userAuthDAO.addAuth(user.getProfile().getUserid(), user.getStuid(), user.getPassword());
        return result;
    }

    @Override
    public boolean updateProfile(ProfileModel profile) {
        return userDAO.update(profile);
    }

    @Override
    public boolean updatePassword(String userid, String password) {
        boolean exists = userDAO.existed(userid);
        if(!exists) {
            throw new NotExistException("User not exists, userid: " + userid);
        }
        return userAuthDAO.updatePassword(userid, password);
    }

}
