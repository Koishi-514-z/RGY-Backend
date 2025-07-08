package org.example.rgybackend.DAO.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.DTO.MoodData;
import org.example.rgybackend.DTO.TimeData;
import org.example.rgybackend.Entity.Emotion;
import org.example.rgybackend.Entity.Tag;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Repository.EmotionRepository;
import org.example.rgybackend.Repository.TagRepository;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmotionDAOImpl implements EmotionDAO {
    @Autowired
    private EmotionRepository emotionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public boolean emotionExists(String userid, LocalDate date) {
        Long timestamp = TimeUtil.getStartOfDayTimestamp(date);
        List<Emotion> emotions = emotionRepository.scanEmotion(userid, timestamp, timestamp + TimeUtil.DAY);
        return !emotions.isEmpty();
    }

    @Override
    public EmotionModel getEmotion(String userid, LocalDate date) {
        Long timestamp = TimeUtil.getStartOfDayTimestamp(date);
        List<Emotion> emotions = emotionRepository.scanEmotion(userid, timestamp, timestamp + TimeUtil.DAY);
        if(emotions.size() == 0) {
            return new EmotionModel(userid, timestamp, null, null);
        }
        if(emotions.size() > 1) {
            throw new RuntimeException("Duplicate emotion");
        }
        return new EmotionModel(emotions.get(0));
    }

    @Override
    public boolean setEmotion(EmotionModel emotionModel) {
        Long timestamp = TimeUtil.getStartOfDayTimestamp(emotionModel.getTimestamp());
        List<Emotion> emotions = emotionRepository.scanEmotion(emotionModel.getUserid(), timestamp, timestamp + TimeUtil.DAY);
        if(emotions.isEmpty()) {
            emotionRepository.save(new Emotion(emotionModel));
        }
        else if(emotions.size() > 1) {
            throw new RuntimeException("Duplicate emotion");
        }
        else {
            Long emotionid = emotions.get(0).getEmotionid();
            Emotion emotion = new Emotion(emotionModel);
            emotion.setEmotionid(emotionid);
            emotionRepository.save(emotion);
        }
        return true;
    }

    @Override
    public List<EmotionDataModel> scanEmotionData(String userid, LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<EmotionDataModel> emotionDataModels = emotionRepository.scanEmotionData(userid, start, end);

        if(emotionDataModels.isEmpty()) {
            return emotionDataModels;
        }

        emotionDataModels.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));
        Long minTimestamp = TimeUtil.getStartOfDayTimestamp(emotionDataModels.get(0).getTimestamp());

        for(EmotionDataModel emotionDataModel : emotionDataModels) {
            Long diffDays = (emotionDataModel.getTimestamp() - minTimestamp) / TimeUtil.DAY;
            emotionDataModel.setTime(diffDays + 1);
        }

        return emotionDataModels;
    }

    @Override
    public EmotionData scanAllData(LocalDate startDate, LocalDate endDate, Long interval) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<EmotionDataModel> emotionDataModels = emotionRepository.scanData(start, end);
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
    public List<TagModel> getTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagModel> tagModels = new ArrayList<>();
        for(Tag tag : tags) {
            tagModels.add(new TagModel(tag));
        }
        return tagModels;
    }

    @Override
    public Map<Long, String> getTagMap() {
        List<TagModel> tagModels = getTags();
        Map<Long, String> tagMap = new HashMap<>();
        for(TagModel tagModel : tagModels) {
            tagMap.put(tagModel.getId(), tagModel.getContent());
        }
        return tagMap;
    }
}
