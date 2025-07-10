package org.example.rgybackend.Model;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Utils.TimeUtil;

public class PushContentMatrix {
    public static final int tagNum = 20;

    public static final double[][] suitabilityMatrix = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 10, 0, 2, 2, 4, 10, 10, 5, 5, 10, 0, 5, 10, 10, 2},
        {0, 10, 5, 10, 2, 2, 5, 2, 10, 8, 10, 3, 0, 0, 10, 0, 10, 0, 10, 10, 2},
        {0, 2, 0, 10, 10, 2, 0, 10, 10, 8, 10, 1, 0, 0, 2, 0, 10, 0, 4, 2, 2},
        {0, 4, 10, 10, 1, 2, 5, 2, 10, 8, 5, 6, 2, 10, 2, 2, 16, 0, 4, 2, 2},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}
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
