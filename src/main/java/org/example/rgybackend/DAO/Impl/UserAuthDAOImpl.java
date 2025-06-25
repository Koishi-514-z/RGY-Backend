package org.example.rgybackend.DAO.Impl;

import java.util.Optional;

import org.example.rgybackend.DAO.UserAuthDAO;
import org.example.rgybackend.Entity.DTO.UserAuthDTO;
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
        Optional<UserAuthDTO> userAuthOptional = userAuthRepository.findById(userid);
        if(userAuthOptional.isEmpty()) {
            return false;
        }
        UserAuthDTO userAuthDTO = userAuthOptional.get();
        return passwordEncoder.matches(password, userAuthDTO.getPassword());
    }

    @Override
    public boolean addAuth(String userid, String stuid, String password) {
        if(userAuthRepository.existsById(userid)) {
            throw new RuntimeException("Duplicate user, userid: " + userid);
        }
        String encodedPassword = passwordEncoder.encode(password);
        UserAuthDTO userAuthDTO = new UserAuthDTO(userid, stuid, encodedPassword);
        userAuthRepository.save(userAuthDTO);
        return true;
    }

    @Override
    public boolean updatePassword(String userid, String password) {
        if(!userAuthRepository.existsById(userid)) {
            throw new NotExistException("User not exists, userid: " + userid);
        }
        Optional<UserAuthDTO> userAuthOptional = userAuthRepository.findById(userid);
        if(userAuthOptional.isEmpty()) {
            return false;
        }
        UserAuthDTO userAuthDTO = userAuthOptional.get();
        userAuthDTO.setPassword(password);
        userAuthRepository.save(userAuthDTO);
        return true;
    }

    @Override
    public boolean removeAuth(String userid) {
        userAuthRepository.deleteById(userid);
        return true;
    }
}
