package org.example.rgybackend.Model;

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
}
