package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionModel {
    private Long emotionid;
    private String userid;
    private Long timestamp;
    private TagModel tag;
    private Long score;
}
