package org.example.rgybackend.Service.Impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.DAO.LikeDAO;
import org.example.rgybackend.DAO.PsyExtraDAO;
import org.example.rgybackend.DAO.ReplyDAO;
import org.example.rgybackend.DAO.UserAuthDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.DTO.IntimateDTO;
import org.example.rgybackend.DTO.LikeData;
import org.example.rgybackend.DTO.ProfileTag;
import org.example.rgybackend.DTO.ReplyData;
import org.example.rgybackend.Entity.PsyProfileExtra;
import org.example.rgybackend.Model.*;
import org.example.rgybackend.Service.MilestoneServive;
import org.example.rgybackend.Service.SjtuMailService;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.AuthCodeManager;
import org.example.rgybackend.Utils.CacheUtil;
import org.example.rgybackend.Utils.ImageCompressor;
import org.example.rgybackend.Utils.NotExistException;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private BlogDAO blogDAO;

    @Autowired
    private LikeDAO likeDAO;

    @Autowired
    private ReplyDAO replyDAO;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private ImageCompressor imageCompressor;

    @Autowired
    private MilestoneServive milestoneServive;

    @Autowired
    private SjtuMailService sjtuMailService;

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

    // 获取用户的个人信息（自动缓存）
    @Override
    public ProfileModel getUserProfile(String userid) {
        ProfileModel cachedProfile = cacheUtil.getProfileFromCache(userid);
        if(cachedProfile != null) {
            return cachedProfile;
        }
        ProfileModel profileModel = userDAO.get(userid);
        profileModel.calcLevel(blogDAO.getBlogsByUserid(userid), replyDAO.findOppositeUser(userid), likeDAO.findOppositeUser(userid));

        if(profileModel.getLevel() > 20) {
            milestoneServive.addMilestone(userid, 7L);
        }

        cacheUtil.putProfileToCache(userid, profileModel);
        return profileModel;
    }

    // 获取所有用户的个人信息（自动缓存）
    @Override
    public List<AdminProfileModel> getAllProfile(String adminid) {
        List<ProfileModel> profileModels = new ArrayList<>();
        List<ProfileTag> profileTags = userDAO.getAllProfileTags();
        for(ProfileTag profileTag : profileTags) {
            profileModels.add(this.getUserProfile(profileTag.getUserid()));
        }

        List<AdminProfileModel> adminProfileModels = new ArrayList<>();
        for(ProfileModel profileModel : profileModels) {
            if(profileModel.getRole() == 1 && profileModel.getUserid().equals(adminid) || profileModel.getUserid().equals("System")) {
                continue;
            }
            AdminProfileModel adminProfileModel = new AdminProfileModel(profileModel, userAuthDAO.isDisabled(profileModel.getUserid())? 1 : 0);
            adminProfileModels.add(adminProfileModel);
        }
        return adminProfileModels;
    }

    // 获取心理咨询师个人信息（自动缓存）
    @Override
    public PsyProfileModel getPsyProfile(String psyid) {
        PsyProfileModel cachedProfile = cacheUtil.getPsyProfileFromCache(psyid);
        if(cachedProfile != null) {
            return cachedProfile;
        }
        ProfileModel profileModel = userDAO.get(psyid);
        PsyProfileExtra profileExtra = psyExtraDAO.getPsyProfileExtra(psyid);
        PsyProfileModel psyProfileModel = new PsyProfileModel(profileExtra);
        psyProfileModel.setUsername(profileModel.getUsername());
        psyProfileModel.setEmail(profileModel.getEmail());
        psyProfileModel.setAvatar(profileModel.getAvatar());
        psyProfileModel.setJointime(profileModel.getJointime());
        cacheUtil.putPsyProfileToCache(psyid, psyProfileModel);
        return psyProfileModel;
    }

    // 获取所有心理咨询师个人信息（自动缓存）
    @Override
    public List<PsyProfileModel> getPsyProfiles() {
        List<ProfileTag> profileTags = userDAO.getPsyProfileTags();
        List<PsyProfileModel> psyProfileModels = new ArrayList<>();
        for(ProfileTag profileTag : profileTags) {
            psyProfileModels.add(this.getPsyProfile(profileTag.getUserid()));
        }
        return psyProfileModels;
    }

    @Override
    public ProfileModel getProfileByName(String username) {
        return userDAO.getByName(username);
    }

    // 获取简化后的个人信息（缓存）
    @Override
    public SimplifiedProfileModel getSimplifiedProfile(String userid) {
        ProfileModel cachedProfile = cacheUtil.getProfileFromCache(userid);
        if(cachedProfile != null) {
            return new SimplifiedProfileModel(cachedProfile);
        }
        ProfileModel profile = userDAO.get(userid);
        cacheUtil.putProfileToCache(userid, profile);
        return new SimplifiedProfileModel(profile);
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
            SimplifiedProfileModel profile = this.getSimplifiedProfile(maxUserid);
            intimateUsers.add(new IntimateDTO(maxScore, profile));
            intimateScores.remove(maxUserid);
        }

        return intimateUsers;
    }

    @Override
    public boolean isDisabled(String username) {
        boolean existed = userDAO.existedByName(username);
        if(!existed) {
            return true;
        }
        String userid = userDAO.getByName(username).getUserid();
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
        milestoneServive.addMilestone(user.getProfile().getUserid(), 1L);
        if(user.getProfile().getRole() != 2) {
            return result && resultAuth;
        }
        PsyProfileExtra profileExtra = new PsyProfileExtra(user.getProfile().getUserid());
        boolean resultExtra = psyExtraDAO.setPsyProfileExtra(profileExtra);
        return result && resultAuth && resultExtra;
    }

    @Override
    public boolean updateProfile(ProfileModel profile) {
        cacheUtil.evictProfileCache(profile.getUserid());

        String avatar = profile.getAvatar();
        if(avatar != null) {
            String compressedAvatar;
            try {
                compressedAvatar = imageCompressor.compressBase64Image(avatar);
            } catch (IOException e) {
                throw new RuntimeException("图片压缩失败", e);
            }
            profile.setAvatar(compressedAvatar);
        }

        return userDAO.update(profile);
    }

    @Override
    public boolean updatePsyProfile(PsyProfileModel psyProfileModel) {
        String psyid = psyProfileModel.getUserid();
        cacheUtil.evictPsyProfileCache(psyid);

        String avatar = psyProfileModel.getAvatar();
        String compressedAvatar;
        try {
            compressedAvatar = imageCompressor.compressBase64Image(avatar);
        } catch (IOException e) {
            throw new RuntimeException("图片压缩失败", e);
        }
        psyProfileModel.setAvatar(compressedAvatar);

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

    @Override
    public boolean postAuthCode(String email) {
        Long authCode = AuthCodeManager.addAuthCode(email);
        if(authCode == null) {
            return false;
        }
        String subject = "【心理健康互助社区】您的验证码";
        String content = "您好，\n\n"
            + "您正在进行邮箱验证操作，您的验证码为： " + authCode + "\n\n"
            + "请在5分钟内完成验证。请勿将验证码泄露给他人。\n\n"
            + "如非本人操作，请忽略此邮件。\n\n"
            + "—— 心理健康互助社区团队";
        sjtuMailService.sendSimpleMail(email, subject, content);
        return true;
    }   

    @Override
    public boolean checkAuthCode(String email, Long authCode) {
        return AuthCodeManager.checkAuthCode(email, authCode);
    }

}
