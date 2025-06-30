package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;

public interface EmotionService {
    EmotionModel getEmotion(String userid);
    
    List<TagModel> getTags();

    List<UrlDataModel> getUrlDatas(String userid);

    List<UrlDataModel> getAllUrlDatas();

    boolean checkNegative(String userid);

    DiaryModel getDiary(String userid);

    List<EmotionDataModel> getWeekData(String userid);

    List<EmotionDataModel> getMonthData(String userid);

    boolean BookCounseling(Long timestamp);

    boolean setEmotion(EmotionModel emotionModel);

    boolean updateDiary(String userid, String content);
}
