package org.example.rgybackend.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionAdminData {
    private Double averageScore;
    private List<MoodData> moodDatas;
}
