package org.example.rgybackend.Entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
    private Long diaryid;
    private String userid;
    private Date timestamp;
    private Long label;
    private String content;
}
