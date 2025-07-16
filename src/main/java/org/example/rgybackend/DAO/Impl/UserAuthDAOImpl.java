package org.example.rgybackend.DAO.Impl;

import java.util.Optional;

import org.example.rgybackend.DAO.UserAuthDAO;
import org.example.rgybackend.Entity.UserAuth;
import org.example.rgybackend.Repository.UserAuthRepository;
import org.example.rgybackend.Utils.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserAuthDAOImpl implements UserAuthDAO {
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean pwdMatch(String userid, String password) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findById(userid);
        if(userAuthOptional.isEmpty()) {
            return false;
        }
        UserAuth userAuthDTO = userAuthOptional.get();
        return passwordEncoder.matches(password, userAuthDTO.getPassword());
    }

    @Override
    public boolean addAuth(String userid, String stuid, String password) {
        if(userAuthRepository.existsById(userid)) {
            throw new RuntimeException("Duplicate user, userid: " + userid);
        }
        String encodedPassword = passwordEncoder.encode(password);
        String encodedStuid = passwordEncoder.encode(stuid);
        UserAuth userAuth = new UserAuth(userid, encodedStuid, encodedPassword, false);
        userAuthRepository.save(userAuth);
        return true;
    }

    @Override
    public boolean updatePassword(String userid, String password) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findById(userid);
        if(userAuthOptional.isEmpty()) {
            return false;
        }
        UserAuth userAuth = userAuthOptional.get();
        String encodedPassword = passwordEncoder.encode(password);
        userAuth.setPassword(encodedPassword);
        userAuthRepository.save(userAuth);
        return true;
    }

    @Override
    public boolean isDisabled(String userid) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findById(userid);
        if(userAuthOptional.isEmpty()) {
            throw new NotExistException("User not exists, userid: " + userid);
        }
        UserAuth userAuth = userAuthOptional.get();
        return userAuth.isDisabled();
    }

    @Override
    public boolean setDisabled(String userid, boolean disabled) {
        Optional<UserAuth> userAuthOptional = userAuthRepository.findById(userid);
        if(userAuthOptional.isEmpty()) {
            throw new NotExistException("User not exists, userid: " + userid);
        }
        UserAuth userAuth = userAuthOptional.get();
        userAuth.setDisabled(disabled);
        userAuthRepository.save(userAuth);
        return true;
    }
}
