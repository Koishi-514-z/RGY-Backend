package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DAO.CrisisAuditingDAO;
import org.example.rgybackend.DAO.DiaryDAO;
import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.DTO.AdminDataDTO;
import org.example.rgybackend.DTO.EmotionAdminData;
import org.example.rgybackend.DTO.MoodData;
import org.example.rgybackend.Model.CrisisAuditingModel;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Service.EmotionService;
import org.example.rgybackend.Utils.BERTModel;
import org.example.rgybackend.Utils.ModelResponse;
import org.example.rgybackend.Utils.NotificationUtil;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmotionServiceImpl implements EmotionService {
    @Autowired
    private EmotionDAO emotionDAO;

    @Autowired
    private DiaryDAO diaryDAO;

    @Autowired
    private BERTModel bertModel;

    @Autowired
    private NotificationPrivateDAO notificationPrivateDAO;

    @Autowired
    private CrisisAuditingDAO crisisAuditingDAO;

    @Override
    public EmotionModel getEmotion(String userid) {
        return emotionDAO.getEmotion(userid, TimeUtil.today());
    }

    @Override
    public List<TagModel> getTags() {
        return emotionDAO.getTags();
    }

    @Override
    public boolean checkNegative(String userid) {
        LocalDate today = TimeUtil.today();
        List<Long> labels = diaryDAO.scanLabel(userid, today.minusDays(2), today);
        if(labels.size() > 3) {
            throw new RuntimeException("Duplicate diary");
        }
        if(labels.size() < 3) {
            return false;
        }
        boolean negative = true;
        for(Long label : labels) {
            negative = negative && (label == 2);
        }
        return negative;
    }

    @Override
    public DiaryModel getDiary(String userid) {
        return diaryDAO.getDiary(userid, TimeUtil.today());
    }

    @Override
    public List<EmotionDataModel> getWeekData(String userid) {
        return emotionDAO.scanEmotionData(userid, TimeUtil.firstDayOfWeek(), TimeUtil.today());
    }

    @Override
    public List<EmotionDataModel> getMonthData(String userid) {
        return emotionDAO.scanEmotionData(userid, TimeUtil.firstDayOfMonth(), TimeUtil.today());
    }

    @Override
    public EmotionAdminData scanAdminData(Long start, Long end) {
        List<AdminDataDTO> adminDatas = emotionDAO.scanAdminData(TimeUtil.getLocalDate(start), TimeUtil.getLocalDate(end));
        Double averageScore = 0.0;
        List<MoodData> moodDatas = new ArrayList<>();

        List<TagModel> tagModels = getTags();
        Map<Long, Long> datas = new HashMap<>();
        for(TagModel tagModel : tagModels) {
            datas.put(tagModel.getId(), 0L);
        }

        for(AdminDataDTO adminData : adminDatas) {
            averageScore += adminData.getScore();
            Long num = datas.get(adminData.getTagid());
            datas.put(adminData.getTagid(), num + 1);
        }

        averageScore /= adminDatas.size();
        for(TagModel tagModel : tagModels) {
            MoodData moodData = new MoodData(tagModel.getContent(), (double)datas.get(tagModel.getId()) / adminDatas.size());
            moodDatas.add(moodData);
        }

        return new EmotionAdminData(averageScore, moodDatas);
    }

    @Override
    public boolean setEmotion(EmotionModel emotionModel) {
        emotionModel.setTimestamp(TimeUtil.now());
        return emotionDAO.setEmotion(emotionModel);
    }

    @Override
    public boolean updateDiary(String userid, String content) {
        ModelResponse emotionResponse = bertModel.checkEmotion(content);
        ModelResponse crisisResponse = bertModel.checkCrisis(content);

        if(crisisResponse.getPredicted_class() == 1) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(userid);
            notificationPrivateDAO.addNotification(notification);
        }

        else if(crisisResponse.getPredicted_class() == 2) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
            notification.setAdminid("System");
            notification.setUserid(userid);
            notificationPrivateDAO.addNotification(notification);
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, userid, content, TimeUtil.now());
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
        }

        DiaryModel diaryModel = new DiaryModel(userid, TimeUtil.now(), emotionResponse.getPredicted_class(), content);
        return diaryDAO.setDiary(diaryModel);
    }
}
