package org.example.rgybackend.DAO.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DAO.EmotionDAO;
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
    public List<EmotionModel> getAllUserEmotion(String userid) {
        List<Emotion> emotions = emotionRepository.findByUserid(userid);
        List<EmotionModel> emotionModels = new ArrayList<>();
        for(Emotion emotion : emotions) {
            emotionModels.add(new EmotionModel(emotion));
        }
        return emotionModels;
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
    public List<EmotionModel> scanEmotion(String userid, LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<Emotion> emotions = emotionRepository.scanEmotion(userid, start, end);
        List<EmotionModel> emotionModels = new ArrayList<>();
        for(Emotion emotion : emotions) {
            emotionModels.add(new EmotionModel(emotion));
        }
        return emotionModels;
    }

    @Override
    public List<EmotionModel> scanAllEmotion(LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<Emotion> emotions = emotionRepository.scanAllEmotion(start, end);
        List<EmotionModel> emotionModels = new ArrayList<>();
        for(Emotion emotion : emotions) {
            emotionModels.add(new EmotionModel(emotion));
        }
        return emotionModels;
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
    public List<EmotionDataModel> scanAllData(LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        return emotionRepository.scanData(start, end);
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
