package org.example.rgybackend.DTO;

import java.time.LocalDate;
import java.util.Optional;

import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Utils.TimeUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionRecord {
    private TagModel tag;
    private Long score;
    private String diary;
    private Long label;
    private Long timestamp;

    public static EmotionRecord getEmotionRecord(Optional<EmotionModel> emotionOptional, Optional<DiaryModel> diaryOptional, LocalDate date) {
        EmotionModel emotionModel = emotionOptional.isPresent() ? emotionOptional.get() : null;
        DiaryModel diaryModel = diaryOptional.isPresent() ? diaryOptional.get() : null;
        if(emotionModel != null && diaryModel == null) {
            return new EmotionRecord(emotionModel.getTag(), emotionModel.getScore(), null, null, TimeUtil.getStartOfDayTimestamp(date));
        }
        else if(emotionModel == null && diaryModel != null) {
            return new EmotionRecord(null, null, diaryModel.getContent(), diaryModel.getLabel(), TimeUtil.getStartOfDayTimestamp(date));
        }
        else if (emotionModel != null && diaryModel != null) {
            return new EmotionRecord(emotionModel.getTag(), emotionModel.getScore(), diaryModel.getContent(), diaryModel.getLabel(), TimeUtil.getStartOfDayTimestamp(date));
        } 
        else {
            return null;
        }
    }
}
