package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.rgybackend.DAO.CrisisAuditingDAO;
import org.example.rgybackend.DAO.DiaryDAO;
import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.DTO.DiaryLabelData;
import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.DTO.EmotionRecord;
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
import org.example.rgybackend.Service.MilestoneServive;
import org.example.rgybackend.Utils.BERTModel;
import org.example.rgybackend.Utils.CacheUtil;
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

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private MilestoneServive milestoneServive;

    // 获取某一星期内的情绪数据
    @Override
    public List<EmotionModel> getUserEmotionByWeek(String userid, LocalDate date) {
        LocalDate firstDayOfWeek = TimeUtil.firstDayOfWeek(date);
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);
        List<EmotionModel> cachedEmotions = cacheUtil.getEmotionsFromCache(userid, firstDayOfWeek);
        if(cachedEmotions == null) {
            List<EmotionModel> emotionModels = emotionDAO.scanEmotion(userid, firstDayOfWeek, lastDayOfWeek);
            cacheUtil.putEmotionsToCache(userid, firstDayOfWeek, emotionModels);
            return emotionModels;
        }
        return cachedEmotions;
    }

    // 获取今天的情绪数据
    @Override
    public EmotionModel getEmotion(String userid) {
        LocalDate today = TimeUtil.today();
        List<EmotionModel> emotionModels = this.getUserEmotionByWeek(userid, today);
        for(EmotionModel emotionModel : emotionModels) {
            if(TimeUtil.getLocalDate(emotionModel.getTimestamp()).equals(today)) {
                return emotionModel;
            }
        }
        return new EmotionModel(userid, TimeUtil.now(), null, null);
    }

    // 修改今天的情绪数据
    @Override
    public boolean setEmotion(EmotionModel emotionModel) {
        LocalDate today = TimeUtil.today();
        LocalDate firstDayOfWeek = TimeUtil.firstDayOfWeek();

        cacheUtil.evictEmotionsCache(emotionModel.getUserid(), firstDayOfWeek);
        cacheUtil.evictAllEmotionsCache(today);
        cacheUtil.evictEmotionRecordsCache(emotionModel.getUserid(), firstDayOfWeek);

        emotionModel.setTimestamp(TimeUtil.now());
        return emotionDAO.setEmotion(emotionModel);
    }
    
    // 获取给定时间段内的情绪数据
    @Override
    public List<EmotionDataModel> scanUserEmotionDatas(String userid, LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<EmotionModel> emotionModels = new ArrayList<>();

        for(LocalDate date = TimeUtil.firstDayOfWeek(startDate); date.compareTo(endDate) <= 0; date = date.plusDays(7)) {
            List<EmotionModel> weeklyDatas = this.getUserEmotionByWeek(userid, date);
            for(EmotionModel emotionModel : weeklyDatas) {
                if(emotionModel.getTimestamp() >= start && emotionModel.getTimestamp() < end) {
                    emotionModels.add(emotionModel);
                }
            }
        }

        return EmotionDataModel.transToDatas(emotionModels);
    }

    @Override
    public List<EmotionDataModel> getWeekData(String userid) {
        LocalDate startDate = TimeUtil.firstDayOfWeek();
        LocalDate endDate = TimeUtil.today();
        return this.scanUserEmotionDatas(userid, startDate, endDate);
    }

    @Override
    public List<EmotionDataModel> getMonthData(String userid) {
        LocalDate startDate = TimeUtil.firstDayOfMonth();
        LocalDate endDate = TimeUtil.today();
        return this.scanUserEmotionDatas(userid, startDate, endDate);
    }



    @Override
    public List<DiaryModel> getUserDiariesByWeek(String userid, LocalDate date) {
        LocalDate firstDayOfWeek = TimeUtil.firstDayOfWeek(date);
        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);
        List<DiaryModel> cachedDiaries = cacheUtil.getDiariesFromCache(userid, firstDayOfWeek);
        if(cachedDiaries == null) {
            List<DiaryModel> diaryModels = diaryDAO.scanDiary(userid, firstDayOfWeek, lastDayOfWeek);
            cacheUtil.putDiariesToCache(userid, firstDayOfWeek, diaryModels);
            return diaryModels;
        }
        return cachedDiaries;
    }

    @Override
    public DiaryModel getDiary(String userid) {
        LocalDate today = TimeUtil.today();
        List<DiaryModel> diaryModels = this.getUserDiariesByWeek(userid, today);
        for(DiaryModel diaryModel : diaryModels) {
            if(TimeUtil.getLocalDate(diaryModel.getTimestamp()).equals(today)) {
                return diaryModel;
            }
        }
        return new DiaryModel(userid, TimeUtil.now(), null, null);
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

        else if(justify == 2 || justify == 3) {
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, userid, content, TimeUtil.now(),0L, 2L, justify - 2);
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
        }

        else if(justify >= 4) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
            notification.setAdminid("System");
            notification.setUserid(userid);
            notificationPrivateDAO.addNotification(notification);
            CrisisAuditingModel crisisAuditingModel = new CrisisAuditingModel(null, userid, content, TimeUtil.now(),0L, 2L, justify - 2);
            crisisAuditingDAO.addCrisis(crisisAuditingModel);
        }

        LocalDate firstDayOfWeek = TimeUtil.firstDayOfWeek();
        cacheUtil.evictDiariesCache(userid, firstDayOfWeek);
        cacheUtil.evictEmotionRecordsCache(userid, firstDayOfWeek);

        DiaryModel diaryModel = new DiaryModel(userid, TimeUtil.now(), emotionResponse.getPredicted_class(), content);
        return diaryDAO.setDiary(diaryModel);
    }

    @Override
    public List<DiaryModel> scanUserDiaries(String userid, LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<DiaryModel> diaryModels = new ArrayList<>();

        for(LocalDate date = TimeUtil.firstDayOfWeek(startDate); date.compareTo(endDate) <= 0; date = date.plusDays(7)) {
            List<DiaryModel> weeklyDatas = this.getUserDiariesByWeek(userid, date);
            for(DiaryModel diaryModel : weeklyDatas) {
                if(diaryModel.getTimestamp() >= start && diaryModel.getTimestamp() < end) {
                    diaryModels.add(diaryModel);
                }
            }
        }

        return diaryModels;
    }

    @Override
    public List<DiaryLabelData> scanUserDiaryLabels(String userid, LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<DiaryLabelData> datas = new ArrayList<>();

        for(LocalDate date = TimeUtil.firstDayOfWeek(startDate); date.compareTo(endDate) <= 0; date = date.plusDays(7)) {
            List<DiaryModel> weeklyDatas = this.getUserDiariesByWeek(userid, date);
            for(DiaryModel diaryModel : weeklyDatas) {
                if(diaryModel.getTimestamp() >= start && diaryModel.getTimestamp() < end) {
                    datas.add(new DiaryLabelData(diaryModel.getLabel(), diaryModel.getTimestamp()));
                }
            }
        }

        return datas;
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
        List<DiaryLabelData> diaryLabelDatas = this.scanUserDiaryLabels(userid, prevWeek, today);
        List<EmotionDataModel> emotionDatas = this.scanUserEmotionDatas(userid, prevWeek, today);
        boolean positive = true;

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
            positive = positive && (label == 0);
        }

        for(EmotionDataModel emotionDataModel : emotionDatas) {
            LocalDate date = TimeUtil.getLocalDate(emotionDataModel.getTimestamp());
            int score = emotionDataModel.getScore().intValue() - 1;
            int timeClass = 0;
            if(date.compareTo(today) >= 0) timeClass = 0;
            else if(date.compareTo(prevWeek) >= 0) timeClass = 1;
            else timeClass = 2;
            negativeRate += timeWeight[timeClass] * scoreRate[score];
            positive = positive && (score >= 4);
        }

        if(positive && diaryLabelDatas.size() >= 5 && emotionDatas.size() >= 5) {
            milestoneServive.addMilestone(userid, 4L);
        }

        if(negativeRate > boundary[1]) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.psyAssist);
            notification.setAdminid("System");
            notification.setUserid(userid);
            notificationPrivateDAO.addNotification(notification);
        }

        return negativeRate > boundary[0];
    }

    
    // 获取某天的全部情绪数据
    @Override
    public List<EmotionModel> getAllEmotionsByDate(LocalDate date) {
        List<EmotionModel> cachedEmotions = cacheUtil.getAllEmotionsFromCache(date);
        if(cachedEmotions == null) {
            List<EmotionModel> emotionModels = emotionDAO.scanAllEmotion(date, date);
            cacheUtil.putAllEmotionsToCache(date, emotionModels);
            return emotionModels;
        }
        return cachedEmotions;
    }

    // 统计给定时间段的情绪数据信息
    @Override
    public EmotionData scanEmotionData(Long start, Long end, Long interval) {
        LocalDate startDate = TimeUtil.getLocalDate(start);
        LocalDate endDate = TimeUtil.getLocalDate(end);
        List<EmotionModel> emotionModels = new ArrayList<>();
        List<TagModel> tagModels = getTags();

        for(LocalDate date = startDate; date.compareTo(endDate) <= 0; date = date.plusDays(1)) {
            emotionModels.addAll(this.getAllEmotionsByDate(date));
        }

        List<EmotionDataModel> emotionDataModels = EmotionDataModel.transToDatas(emotionModels);

        EmotionData emotionData = new EmotionData(interval);
        emotionData.setTotalNum((long)emotionDataModels.size());

        if(emotionDataModels.isEmpty()) {
            return emotionData;
        }

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
    public List<EmotionRecord> getRecordsByWeek(String userid, LocalDate date) {
        LocalDate weekDate = TimeUtil.firstDayOfWeek(date);

        List<EmotionRecord> cachedRecords = cacheUtil.getEmotionRecordsFromCache(userid, weekDate);
        if(cachedRecords != null) {
            return cachedRecords;
        }

        List<EmotionModel> emotionModels = this.getUserEmotionByWeek(userid, weekDate);
        List<DiaryModel>  diaryModels = this.getUserDiariesByWeek(userid, weekDate);
        List<EmotionRecord> emotionRecords = new ArrayList<>();

        for(int i = 0; i < 7; ++i) {
            final LocalDate currentDate = weekDate.plusDays(i);
            Optional<EmotionModel> emotionOptional = emotionModels.stream()
                    .filter(e -> TimeUtil.getLocalDate(e.getTimestamp()).equals(currentDate))
                    .findFirst();
            Optional<DiaryModel> diaryOptional = diaryModels.stream()
                    .filter(d -> TimeUtil.getLocalDate(d.getTimestamp()).equals(currentDate))
                    .findFirst();
            EmotionRecord emotionRecord = EmotionRecord.getEmotionRecord(emotionOptional, diaryOptional, currentDate);
            if(emotionRecord != null) {
                emotionRecords.add(emotionRecord);
            }
        }

        cacheUtil.putEmotionRecordsToCache(userid, weekDate, emotionRecords);

        return emotionRecords;
    }

    @Override
    public Long getRecordNum(String userid) {
        LocalDate startDate = TimeUtil.firstDayOfWeek();
        LocalDate endDate = startDate.minusDays(7 * 52 * 3);  
        List<EmotionRecord> emotionRecords = new ArrayList<>();

        for(LocalDate weekDate = startDate; weekDate.compareTo(endDate) > 0; weekDate = weekDate.minusDays(7)) {
            emotionRecords.addAll(this.getRecordsByWeek(userid, weekDate));
        }

        return Long.valueOf(emotionRecords.size());
    }

    @Override
    public List<EmotionRecord> getHistoryRecords(Long pageIndex, Long pageSize, String userid) {
        Long total = pageSize * (pageIndex + 1);
        LocalDate startDate = TimeUtil.firstDayOfWeek();
        LocalDate endDate = startDate.minusDays(7 * 52 * 3);
        List<EmotionRecord> emotionRecords = new ArrayList<>();
        List<EmotionRecord> resultList = new ArrayList<>();

        for(LocalDate weekDate = startDate; weekDate.compareTo(endDate) > 0 && emotionRecords.size() < total; weekDate = weekDate.minusDays(7)) {
            emotionRecords.addAll(this.getRecordsByWeek(userid, weekDate));
        }
        
        emotionRecords.sort((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()));

        for(Long i = pageSize * pageIndex; i < Math.min(total, emotionRecords.size()); ++i) {
            resultList.add(emotionRecords.get(i.intValue()));
        }

        return resultList;
    }
}
