package org.example.rgybackend.DAO.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.Entity.Emotion;
import org.example.rgybackend.Entity.Tag;
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
    public EmotionModel getEmotion(String userid, LocalDate date) {
        Long timestamp = TimeUtil.getStartOfDayTimestamp(date);
        List<Emotion> emotions = emotionRepository.scanEmotion(userid, timestamp, timestamp + TimeUtil.DAY);
        if(emotions.size() == 0) {
            return new EmotionModel(userid, timestamp, null, null);
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
    public List<TagModel> getTags() {
        List<Tag> tags = tagRepository.findAll();
        List<TagModel> tagModels = new ArrayList<>();
        for(Tag tag : tags) {
            tagModels.add(new TagModel(tag));
        }
        return tagModels;
    }
}
