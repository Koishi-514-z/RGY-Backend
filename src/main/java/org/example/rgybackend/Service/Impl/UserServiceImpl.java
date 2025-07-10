package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DAO.LikeDAO;
import org.example.rgybackend.DAO.PsyExtraDAO;
import org.example.rgybackend.DAO.ReplyDAO;
import org.example.rgybackend.DAO.UserAuthDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.DTO.IntimateDTO;
import org.example.rgybackend.DTO.LikeData;
import org.example.rgybackend.DTO.ReplyData;
import org.example.rgybackend.Entity.PsyProfileExtra;
import org.example.rgybackend.Model.*;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.NotExistException;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PsyExtraDAO psyExtraDAO;

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
        return userDAO.getRole(userid) > 0;
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
    @Cacheable(value = "profile", key = "#userid")
    public ProfileModel getUserProfile(String userid) {
        return userDAO.get(userid);
    }

    @Override
    public List<AdminProfileModel> getAllProfile(String adminid) {
        List<ProfileModel> profileModels = userDAO.getAll();
        List<AdminProfileModel> adminProfileModels = new ArrayList<>();
        for(ProfileModel profileModel : profileModels) {
            if(profileModel.getRole() == 1 && profileModel.getUserid().equals(adminid)){
                continue;
            }
            AdminProfileModel adminProfileModel = new AdminProfileModel(profileModel, userAuthDAO.isDisabled(profileModel.getUserid())? 1 : 0);
            adminProfileModels.add(adminProfileModel);
        }
        return adminProfileModels;
    }

    @Override
    @Cacheable(value = "psyprofile", key = "#psyid")
    public PsyProfileModel getPsyProfile(String psyid) {
        ProfileModel profileModel = userDAO.get(psyid);
        PsyProfileExtra profileExtra = psyExtraDAO.getPsyProfileExtra(psyid);
        PsyProfileModel psyProfileModel = new PsyProfileModel(profileExtra);
        psyProfileModel.setUsername(profileModel.getUsername());
        psyProfileModel.setEmail(profileModel.getEmail());
        psyProfileModel.setAvatar(profileModel.getAvatar());
        psyProfileModel.setJointime(profileModel.getJointime());
        return psyProfileModel;
    }

    @Override
    public List<PsyProfileModel> getPsyProfiles() {
        List<ProfileModel> profileModels = userDAO.getAllPsys();
        List<PsyProfileModel> psyProfileModels = new ArrayList<>();
        for(ProfileModel profileModel : profileModels) {
            String psyid = profileModel.getUserid();
            PsyProfileExtra profileExtra = psyExtraDAO.getPsyProfileExtra(psyid);
            PsyProfileModel psyProfileModel = new PsyProfileModel(profileExtra);
            psyProfileModel.setUsername(profileModel.getUsername());
            psyProfileModel.setEmail(profileModel.getEmail());
            psyProfileModel.setAvatar(profileModel.getAvatar());
            psyProfileModel.setJointime(profileModel.getJointime());
            psyProfileModels.add(psyProfileModel);
        }
        return psyProfileModels;
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
    @Cacheable(value = "intimateUsers", key = "#userid")
    public List<IntimateDTO> getIntimateUsers(String userid) {
        List<IntimateDTO> intimateUsers = new ArrayList<>();
        final int total = 4;
        final double likeWeight = 0.6;   
        final double timeWeight[] = {18.0, 9.0, 3.0, 1.0};
        List<LikeData> likeDatas = likeDAO.findOppositeUser(userid);
        List<ReplyData> replyDatas = replyDAO.findOppositeUser(userid);

        LocalDate today = TimeUtil.today();
        LocalDate yesterday = today.minusDays(1);
        LocalDate prevWeek = today.minusDays(7);
        LocalDate prevMonth = today.minusDays(30);

        Map<String, Double> intimateScores = new HashMap<>();
        for(LikeData likeData : likeDatas) {
            intimateScores.put(likeData.getUserid(), 0.0);
        }
        for(ReplyData replyData : replyDatas) {
            intimateScores.put(replyData.getUserid(), 0.0);
        }

        for(LikeData likeData : likeDatas) {
            LocalDate date = TimeUtil.getLocalDate(likeData.getTimestamp());
            int timeClass;
            if(date.compareTo(yesterday) >= 0) timeClass = 0;
            else if(date.compareTo(prevWeek) >= 0) timeClass = 1;
            else if(date.compareTo(prevMonth) >= 0) timeClass = 2;
            else timeClass = 3;
            Double score = likeWeight * timeWeight[timeClass];
            Double originScore = intimateScores.get(likeData.getUserid());
            intimateScores.put(likeData.getUserid(), originScore + score);
        }

        for(ReplyData replyData : replyDatas) {
            LocalDate date = TimeUtil.getLocalDate(replyData.getTimestamp());
            int timeClass;
            if(date.compareTo(yesterday) >= 0) timeClass = 0;
            else if(date.compareTo(prevWeek) >= 0) timeClass = 1;
            else if(date.compareTo(prevMonth) >= 0) timeClass = 2;
            else timeClass = 3;
            Double score = (1 - likeWeight) * timeWeight[timeClass];
            Double originScore = intimateScores.get(replyData.getUserid());
            intimateScores.put(replyData.getUserid(), originScore + score);
        }

        Iterator<Map.Entry<String, Double>> iterator = intimateScores.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            if(entry.getKey().equals(userid)) {
                iterator.remove();
            }
        }
        
        for(int i = 0; i < total; ++i) {
            if(intimateScores.isEmpty()) {
                break;
            }
            String maxUserid = null;
            Double maxScore = -1.0;
            for(Map.Entry<String, Double> data : intimateScores.entrySet()) {
                String datauserid = data.getKey();
                Double score = data.getValue();
                if(score > maxScore) {
                    maxScore = score;
                    maxUserid = datauserid;
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
        user.getProfile().setJointime(TimeUtil.now());
        boolean result = userDAO.add(user.getProfile());
        boolean resultAuth = userAuthDAO.addAuth(user.getProfile().getUserid(), user.getStuid(), user.getPassword());
        if(user.getProfile().getRole() != 2) {
            return result && resultAuth;
        }
        PsyProfileExtra profileExtra = new PsyProfileExtra(user.getProfile().getUserid());
        boolean resultExtra = psyExtraDAO.setPsyProfileExtra(profileExtra);
        return result && resultAuth && resultExtra;
    }

    @Override
    @CacheEvict(value = "profile", key = "#profile.userid")
    public boolean updateProfile(ProfileModel profile) {
        return userDAO.update(profile);
    }

    @Override
    @CacheEvict(value = "psyprofile", key = "#psyProfileModel.userid")
    public boolean updatePsyProfile(PsyProfileModel psyProfileModel) {
        String psyid = psyProfileModel.getUserid();

        ProfileModel newProfileModel = new ProfileModel();
        newProfileModel.setUserid(psyid);
        newProfileModel.setUsername(psyProfileModel.getUsername());
        newProfileModel.setEmail(psyProfileModel.getEmail());
        newProfileModel.setAvatar(psyProfileModel.getAvatar());
        newProfileModel.setNote("");
        newProfileModel.setRole(2L);
        boolean result = userDAO.update(newProfileModel);

        PsyProfileExtra profileExtra = psyExtraDAO.getPsyProfileExtra(psyid);
        PsyProfileExtra newProfileExtra = new PsyProfileExtra(psyProfileModel);
        newProfileExtra.setTotalClients(profileExtra.getTotalClients());
        newProfileExtra.setTotalScore(profileExtra.getTotalScore());
        newProfileExtra.setCommentNum(profileExtra.getCommentNum());
        newProfileExtra.setSuccessNum(profileExtra.getSuccessNum());
        boolean resultExtra = psyExtraDAO.setPsyProfileExtra(newProfileExtra);

        return result && resultExtra;
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
