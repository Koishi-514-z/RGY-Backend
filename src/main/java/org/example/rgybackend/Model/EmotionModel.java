package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Emotion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionModel {
    private String userid;
    private Long timestamp;
    private TagModel tag;
    private Long score;

    public EmotionModel(Emotion emotion) {
        this.userid = emotion.getUserid();
        this.timestamp = emotion.getTimestamp();
        this.tag = new TagModel(emotion.getTag());
        this.score = emotion.getScore();
    }
}
