package org.example.rgybackend.Service;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.DTO.DiaryLabelData;
import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.DTO.EmotionRecord;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;

public interface EmotionService {
    List<EmotionModel> getUserEmotionByWeek(String userid, LocalDate date);

    EmotionModel getEmotion(String userid);

    boolean setEmotion(EmotionModel emotionModel);

    List<EmotionDataModel> scanUserEmotionDatas(String userid, LocalDate startDate, LocalDate endDate);

    List<EmotionDataModel> getWeekData(String userid);

    List<EmotionDataModel> getMonthData(String userid);


    List<DiaryModel> getUserDiariesByWeek(String userid, LocalDate date);

    DiaryModel getDiary(String userid);

    boolean updateDiary(String userid, String content);

    List<DiaryModel> scanUserDiaries(String userid, LocalDate startDate, LocalDate endDate);

    List<DiaryLabelData> scanUserDiaryLabels(String userid, LocalDate startDate, LocalDate endDate);
    

    List<TagModel> getTags();

    List<TagModel> getUrlTags();

    boolean checkNegative(String userid);


    List<EmotionModel> getAllEmotionsByDate(LocalDate date);

    EmotionData scanEmotionData(Long start, Long end, Long interval);


    List<EmotionRecord> getRecordsByWeek(String userid, LocalDate date);

    Long getRecordNum(String userid);

    List<EmotionRecord> getHistoryRecords(Long pageIndex, Long pageSize, String userid);
}
