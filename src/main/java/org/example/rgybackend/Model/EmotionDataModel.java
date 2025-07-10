package org.example.rgybackend.Model;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Utils.TimeUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionDataModel {
    private Long timestamp;
    private Long score;
    private Long tagid;
    private Long time;

    public EmotionDataModel(EmotionModel emotionModel) {
        this.timestamp = emotionModel.getTimestamp();
        this.score = emotionModel.getScore();
        this.tagid = emotionModel.getTag().getId();
        this.time = null;
    }

    public static List<EmotionDataModel> transToDatas(List<EmotionModel> emotionModels) {
        List<EmotionDataModel> emotionDataModels = new ArrayList<>();
        if(emotionModels.isEmpty()) {
            return emotionDataModels;
        }

        for(EmotionModel emotionModel : emotionModels) {
            emotionDataModels.add(new EmotionDataModel(emotionModel));
        }

        emotionDataModels.sort((e1, e2) -> e1.getTimestamp().compareTo(e2.getTimestamp()));

        Long minTimestamp = TimeUtil.getStartOfDayTimestamp(emotionDataModels.get(0).getTimestamp());
        for(EmotionDataModel emotionDataModel : emotionDataModels) {
            Long diffDays = (emotionDataModel.getTimestamp() - minTimestamp) / TimeUtil.DAY;
            emotionDataModel.setTime(diffDays + 1);
        }

        return emotionDataModels;
    }
}
