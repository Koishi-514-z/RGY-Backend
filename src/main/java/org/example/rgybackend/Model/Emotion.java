package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emotion {
    private Long emotionid;
    private String userid;
    private Long timestamp;
    private Tag tag;
    private Long score;
}
