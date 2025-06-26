package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
    private Long diaryid;
    private String userid;
    private Long timestamp;
    private Long label;
    private String content;
}
