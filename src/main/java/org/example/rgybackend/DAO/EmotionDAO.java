package org.example.rgybackend.DAO;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;

public interface EmotionDAO {
    boolean emotionExists(String userid, LocalDate date);

    EmotionModel getEmotion(String userid, LocalDate date);

    boolean setEmotion(EmotionModel emotionModel);

    List<EmotionDataModel> getEmotionData(String userid, LocalDate startDate, LocalDate endDate);

    List<TagModel> getTags();
}
