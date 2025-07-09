package org.example.rgybackend.Model;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Utils.TimeUtil;

public class PushContentMatrix {
    public static final int tagNum = 20;

    public static final double[][] suitabilityMatrix = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 3, 2, 2, 1, 1, 4, 2, 4, 4, 5, 5, 5, 5, 4, 4, 3, 3, 5, 5, 3},
        {0, 5, 5, 5, 5, 4, 3, 4, 4, 4, 3, 3, 2, 2, 4, 3, 5, 4, 3, 5, 3},
        {0, 4, 4, 5, 5, 4, 3, 5, 4, 3, 3, 3, 2, 3, 3, 2, 5, 5, 3, 4, 3},
        {0, 4, 4, 4, 4, 3, 3, 4, 3, 3, 4, 4, 3, 5, 4, 3, 5, 5, 4, 4, 3},
        {0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}
    };

    public static double[] getSuitabilityArray(List<EmotionDataModel> emotionDataModels) {
        double[] suitabilityArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        final double timeWeight[] = {4.0, 2.0, 1.0};
        LocalDate today = TimeUtil.today();
        LocalDate prevThreeDay = today.minusDays(3);

        for(EmotionDataModel emotionDataModel : emotionDataModels) {
            int emoIdx = emotionDataModel.getTagid().intValue();
            LocalDate date = TimeUtil.getLocalDate(emotionDataModel.getTimestamp());
            int timeClass = 0;
            if(date.compareTo(today) >= 0) timeClass = 0;
            else if(date.compareTo(prevThreeDay) >= 0) timeClass = 1;
            else timeClass = 2;
            for(int typeIdx = 1; typeIdx <= tagNum; ++typeIdx) {
                suitabilityArray[typeIdx] += suitabilityMatrix[emoIdx][typeIdx] * timeWeight[timeClass];
            }
        }

        return suitabilityArray;
    }
}
