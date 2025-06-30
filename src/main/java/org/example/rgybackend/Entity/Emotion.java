package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.EmotionModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "emotion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emotion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "emotionid")
    private Long emotionid;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagid", referencedColumnName = "id", insertable=false, updatable=false) 
    private Tag tag;

    @Basic
    @Column(name = "score")
    private Long score;

    public Emotion(EmotionModel emotionModel) {
        this.emotionid = null;
        this.userid = emotionModel.getUserid();
        this.timestamp = emotionModel.getTimestamp();
        this.tag = new Tag(emotionModel.getTag());
        this.score = emotionModel.getScore();
    }
}
