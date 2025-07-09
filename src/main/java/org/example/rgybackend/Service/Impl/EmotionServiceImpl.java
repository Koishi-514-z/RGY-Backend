package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DAO.CrisisAuditingDAO;
import org.example.rgybackend.DAO.DiaryDAO;
import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.DTO.DiaryLabelData;
import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.DTO.MoodData;
import org.example.rgybackend.DTO.TimeData;
import org.example.rgybackend.Model.CrisisAuditingModel;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;
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
    public List<TagModel> getUrlTags() {
        return new UrlDataModel().typeTags;
    }

    @Override
    public boolean checkNegative(String userid) {
        LocalDate today = TimeUtil.today();
        LocalDate prevThreeDay = today.minusDays(3);
        LocalDate prevWeek = today.minusDays(7);
        List<DiaryLabelData> diaryLabelDatas = diaryDAO.scanLabel(userid, prevWeek, today);
        List<EmotionDataModel> emotionDatas = emotionDAO.scanAllData(prevWeek, today);

        final double labelRate[] = {-2.0, 0.0, 4.0};
        final double scoreRate[] = {6.0, 3.0, 0.0, -1.0, -3.0}; 
        final double timeWeight[] = {4.0, 2.0, 1.0};
        final double boundary[] = {48.0, 64.0};
        double negativeRate = 0.0;

        for(DiaryLabelData diaryLabelData : diaryLabelDatas) {
            LocalDate date = TimeUtil.getLocalDate(diaryLabelData.getTimestamp());
            int label = diaryLabelData.getLabel().intValue();
            int timeClass = 0;
            if(date.compareTo(today) >= 0) timeClass = 0;
            else if(date.compareTo(prevThreeDay) >= 0) timeClass = 1;
            else timeClass = 2;
            negativeRate += timeWeight[timeClass] * labelRate[label];
        }

        for(EmotionDataModel emotionDataModel : emotionDatas) {
            LocalDate date = TimeUtil.getLocalDate(emotionDataModel.getTimestamp());
            int score = emotionDataModel.getScore().intValue() - 1;
            int timeClass = 0;
            if(date.compareTo(today) >= 0) timeClass = 0;
            else if(date.compareTo(prevWeek) >= 0) timeClass = 1;
            else timeClass = 2;
            negativeRate += timeWeight[timeClass] * scoreRate[score];
        }

        if(negativeRate > boundary[1]) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(userid);
            notificationPrivateDAO.addNotification(notification);
        }

        return negativeRate > boundary[0];
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
    public EmotionData scanEmotionData(Long start, Long end, Long interval) {
        LocalDate startDate = TimeUtil.getLocalDate(start);
        LocalDate endDate = TimeUtil.getLocalDate(end);
        List<EmotionDataModel> emotionDataModels = emotionDAO.scanAllData(startDate, endDate);
        List<TagModel> tagModels = getTags();

        EmotionData emotionData = new EmotionData(interval);
        emotionData.setTotalNum((long)emotionDataModels.size());

        if(emotionDataModels.isEmpty()) {
            return emotionData;
        }

        emotionDataModels.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));
        Long minTimestamp = TimeUtil.getStartOfDayTimestamp(emotionDataModels.get(0).getTimestamp());
        Long maxTimestamp = TimeUtil.getStartOfDayTimestamp(emotionDataModels.get(emotionDataModels.size() - 1).getTimestamp()) + TimeUtil.DAY;
        emotionData.setStartDate(minTimestamp);
        emotionData.setEndDate(maxTimestamp);
        emotionData.setTotalDate((maxTimestamp - minTimestamp) / TimeUtil.DAY);

        Long num = 0L, score = 0L, lastSlots = 0L;
        Long pos = 0L, neu = 0L, neg = 0L;
        Long totalScore = 0L;

        for(int i = 0; i < emotionDataModels.size(); ++i) {
            EmotionDataModel emotionDataModel = emotionDataModels.get(i);
            totalScore += emotionDataModel.getScore();
            Long diffSlots = (emotionDataModel.getTimestamp() - minTimestamp) / (interval * TimeUtil.DAY);
            if(diffSlots == lastSlots) {
                score += emotionDataModel.getScore();
                num++;
                if(emotionDataModel.getScore() >= 4) pos++;
                else if(emotionDataModel.getScore() >= 3) neu++;
                else neg++;
            }
            else {
                emotionData.getTimeDatas().add(new TimeData(lastSlots, num, score * 1.0 / num, pos, neu, neg));
                num = 1L;
                score = emotionDataModel.getScore();
                pos = neu = neg = 0L;
                if(emotionDataModel.getScore() >= 4) pos++;
                else if(emotionDataModel.getScore() >= 3) neu++;
                else neg++;
                lastSlots = diffSlots;
            }
        }
        emotionData.getTimeDatas().add(new TimeData(lastSlots, num, score * 1.0 / num, pos, neu, neg));
        emotionData.setAvgScore(totalScore * 1.0 / emotionDataModels.size());

        Map<Long, Long> radioDataMap = new HashMap<>();
        for(TagModel tagModel : tagModels) {
            radioDataMap.put(tagModel.getId(), 0L);
        }

        for(int i = 0; i < emotionDataModels.size(); ++i) {
            EmotionDataModel emotionDataModel = emotionDataModels.get(i);
            Long oldNum = radioDataMap.get(emotionDataModel.getTagid());
            radioDataMap.put(emotionDataModel.getTagid(), oldNum + 1);
        }

        for(TagModel tagModel : tagModels) {
            Long total = radioDataMap.get(tagModel.getId());
            emotionData.getRatioDatas().add(new MoodData(tagModel, total, total * 100.0 / emotionDataModels.size()));
        }

        return emotionData;
    }

    @Override
    public boolean setEmotion(EmotionModel emotionModel) {
        emotionModel.setTimestamp(TimeUtil.now());
        return emotionDAO.setEmotion(emotionModel);
    }

    @Override
    public boolean updateDiary(String userid, String content) {
        ModelResponse emotionResponse = bertModel.checkEmotion(content);
        Long justify = bertModel.justify(content);

        if(justify == 1) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(userid);
            notificationPrivateDAO.addNotification(notification);
        }

        else if(justify == 2) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
            notification.setAdminid("System");
            notification.setUserid(userid);
            notificationPrivateDAO.addNotification(notification);
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, userid, content, TimeUtil.now(),0L,2L);
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
        }

        DiaryModel diaryModel = new DiaryModel(userid, TimeUtil.now(), emotionResponse.getPredicted_class(), content);
        return diaryDAO.setDiary(diaryModel);
    }
}
