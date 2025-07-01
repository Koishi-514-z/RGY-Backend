package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DAO.LikeDAO;
import org.example.rgybackend.DAO.ReplyDAO;
import org.example.rgybackend.DAO.UserAuthDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.DTO.IntimateDTO;
import org.example.rgybackend.DTO.LikeData;
import org.example.rgybackend.DTO.ReplyData;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Model.UserModel;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.NotExistException;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserAuthDAO userAuthDAO;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private LikeDAO likeDAO;

    @Autowired
    private ReplyDAO replyDAO;

    @Value("${verify.admin.key}")
    private String KEY;

    @Override
    public boolean isAdmin(String userid) {
        ProfileModel profile = userDAO.get(userid);
        return profile.getRole() > 0;
    }

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
    public List<IntimateDTO> getIntimateUsers(String userid) {
        List<IntimateDTO> intimateUsers = new ArrayList<>();
        final int total = 4;
        final double likeWeight = 0.5;   
        final double timeWeight[] = {9.0, 3.0, 1.0};
        List<LikeData> likeDatas = likeDAO.findOppositeUser(userid);
        List<ReplyData> replyDatas = replyDAO.findOppositeUser(userid);

        LocalDate today = TimeUtil.today();
        LocalDate prevWeek = today.minusDays(7);
        LocalDate prevMonth = today.minusDays(30);

        Map<String, Double> intimateScores = new HashMap<>();
        for(LikeData likeData : likeDatas) {
            intimateScores.put(likeData.getTouserid(), 0.0);
        }
        for(ReplyData replyData : replyDatas) {
            intimateScores.put(replyData.getTouserid(), 0.0);
        }

        for(LikeData likeData : likeDatas) {
            LocalDate date = TimeUtil.getLocalDate(likeData.getTimestamp());
            int timeClass;
            if(date.compareTo(prevWeek) >= 0) {
                timeClass = 2;
            }
            else if(date.compareTo(prevMonth) >= 0) {
                timeClass = 1;
            }
            else {
                timeClass = 0;
            }
            Double score = likeWeight * timeWeight[timeClass];
            Double originScore = intimateScores.get(likeData.getTouserid());
            intimateScores.put(likeData.getTouserid(), originScore + score);
        }

        for(ReplyData replyData : replyDatas) {
            LocalDate date = TimeUtil.getLocalDate(replyData.getTimestamp());
            int timeClass;
            if(date.compareTo(prevWeek) >= 0) {
                timeClass = 2;
            }
            else if(date.compareTo(prevMonth) >= 0) {
                timeClass = 1;
            }
            else {
                timeClass = 0;
            }
            Double score = (1 - likeWeight) * timeWeight[timeClass];
            Double originScore = intimateScores.get(replyData.getTouserid());
            intimateScores.put(replyData.getTouserid(), originScore + score);
        }

        for(int i = 0; i < Math.min(total, intimateScores.size()); ++i) {
            String maxUserid = null;
            Double maxScore = -1.0;
            for (Map.Entry<String, Double> data : intimateScores.entrySet()) {
                String touserid = data.getKey();
                Double score = data.getValue();
                if(score > maxScore) {
                    maxScore = score;
                    maxUserid = touserid;
                }
            }
            SimplifiedProfileModel profile = userDAO.getSimplified(maxUserid);
            intimateUsers.add(new IntimateDTO(maxScore, profile));
            intimateScores.remove(maxUserid);
        }

        return intimateUsers;
    }

    @Override
    public boolean isDisabled(String userid) {
        return userAuthDAO.isDisabled(userid);
    }

    @Override
    public boolean verifyPassword(String userid, String password) {
        return userAuthDAO.pwdMatch(userid, password);
    }

    @Override
    public boolean verifyAdmin(String verifyKey) {
        return encoder.matches(verifyKey, KEY);
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

    @Override
    public boolean setDisabled(String userid, boolean disabled) {
        return userAuthDAO.setDisabled(userid, disabled);
    }

}
