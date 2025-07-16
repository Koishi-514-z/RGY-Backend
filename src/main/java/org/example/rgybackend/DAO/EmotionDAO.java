package org.example.rgybackend.DAO;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;

public interface EmotionDAO {
    EmotionModel getEmotion(String userid, LocalDate date);

    boolean setEmotion(EmotionModel emotionModel);

    List<EmotionModel> scanEmotion(String userid, LocalDate startDate, LocalDate endDate);

    List<EmotionModel> scanAllEmotion(LocalDate startDate, LocalDate endDate);

    List<TagModel> getTags();
}
