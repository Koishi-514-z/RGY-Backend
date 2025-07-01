package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.DiaryModel;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diary {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "diaryid")
    private Long diaryid;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "label")
    private Long label;

    @Basic
    @Column(name = "content")
    private String content;

    public Diary(DiaryModel diaryModel) {
        this.diaryid = null;
        this.userid = diaryModel.getUserid();
        this.timestamp = diaryModel.getTimestamp();
        this.label = diaryModel.getLabel();
        this.content = diaryModel.getContent();
    }
}
