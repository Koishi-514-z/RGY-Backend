package org.example.rgybackend.DAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;

public interface EmotionDAO {
    boolean emotionExists(String userid, LocalDate date);

    EmotionModel getEmotion(String userid, LocalDate date);

    boolean setEmotion(EmotionModel emotionModel);

    List<EmotionDataModel> scanEmotionData(String userid, LocalDate startDate, LocalDate endDate);

    EmotionData scanAllData(LocalDate startDate, LocalDate endDate, Long interval);

    List<TagModel> getTags();

    Map<Long, String> getTagMap();
}
