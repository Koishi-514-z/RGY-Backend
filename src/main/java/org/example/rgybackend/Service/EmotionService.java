package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;

public interface EmotionService {
    EmotionModel getEmotion(String userid);
    
    List<TagModel> getTags();

    List<TagModel> getUrlTags();

    boolean checkNegative(String userid);

    DiaryModel getDiary(String userid);

    List<EmotionDataModel> getWeekData(String userid);

    List<EmotionDataModel> getMonthData(String userid);

    EmotionData scanEmotionData(Long start, Long end, Long interval);

    boolean setEmotion(EmotionModel emotionModel);

    boolean updateDiary(String userid, String content);
}
