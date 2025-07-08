package org.example.rgybackend.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionData {
    private Long startDate;
    private Long endDate;
    private Long totalDate;
    private Long interval;
    private Long totalNum;
    private Double avgScore;
    private List<TimeData> timeDatas;
    private List<MoodData> ratioDatas;

    public EmotionData(Long interval) {
        this.startDate = 0L;
        this.endDate = 0L;
        this.totalDate = 0L;
        this.interval = interval;
        this.totalNum = 0L;
        this.avgScore = 0.0;
        this.timeDatas = new ArrayList<>();
        this.ratioDatas = new ArrayList<>();
    }
}
