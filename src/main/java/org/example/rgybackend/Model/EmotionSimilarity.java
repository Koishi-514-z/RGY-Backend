package org.example.rgybackend.Model;

import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


public class EmotionSimilarity {
    private List<List<Integer>> emotion1;
    public EmotionSimilarity() {
        for (int i = 0; i < 5; i++) {
            emotion1.add(new ArrayList<>());
            for (int j = 0; j < 5; j++) {
                emotion1.get(i).add(0);
            }
        }
        emotion1.get(0).set(0, 5);
        emotion1.get(0).set(1, 1);
        emotion1.get(0).set(2, 1);
        emotion1.get(0).set(3, 1);
        emotion1.get(0).set(4, 3);
        emotion1.get(1).set(0, 1);
        emotion1.get(1).set(1, 5);
        emotion1.get(1).set(2, 4);
        emotion1.get(1).set(3, 3);
        emotion1.get(1).set(4, 2);
        emotion1.get(2).set(0, 1);
        emotion1.get(2).set(1, 4);
        emotion1.get(2).set(2, 5);
        emotion1.get(2).set(3, 4);
        emotion1.get(2).set(4, 2);
        emotion1.get(3).set(0, 1);
        emotion1.get(3).set(1, 3);
        emotion1.get(3).set(2, 4);
        emotion1.get(3).set(3, 5);
        emotion1.get(3).set(4, 2);
        emotion1.get(4).set(0, 3);
        emotion1.get(4).set(1, 2);
        emotion1.get(4).set(2, 2);
        emotion1.get(4).set(3, 2);
        emotion1.get(4).set(4, 5);

    }
    public List<List<Integer>> getEmotion1() {
        return emotion1;
    }
    public void setEmotion1(List<List<Integer>> emotion1) {
        this.emotion1 = emotion1;
    }
}
