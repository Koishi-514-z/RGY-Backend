package org.example.rgybackend.Entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emotion {
    private Long emotionid;
    private String userid;
    private Date timestamp;
    private Tag tag;
    private Long score;
}
