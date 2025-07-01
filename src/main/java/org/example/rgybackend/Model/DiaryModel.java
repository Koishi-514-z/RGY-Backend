package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Diary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryModel {
    private String userid;
    private Long timestamp;
    private Long label;
    private String content;

    public DiaryModel(Diary diary) {
        this.userid = diary.getUserid();
        this.timestamp = diary.getTimestamp();
        this.label = diary.getLabel();
        this.content = diary.getContent();
    }
}
