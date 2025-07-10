package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.Entity.Emotion;
import org.example.rgybackend.Model.EmotionDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    List<Emotion> findByUserid(String userid);

    @Query("SELECT e FROM Emotion e WHERE e.userid = :userid AND e.timestamp >= :start AND e.timestamp < :end")
    List<Emotion> scanEmotion(String userid, Long start, Long end);

    @Query("SELECT e FROM Emotion e WHERE e.timestamp >= :start AND e.timestamp < :end")
    List<Emotion> scanAllEmotion(Long start, Long end);

    @Query("SELECT new org.example.rgybackend.Model.EmotionDataModel(e.timestamp, e.score, e.tagid, null) FROM Emotion e WHERE e.userid = :userid")
    List<EmotionDataModel> findEmotionData(String userid);

    @Query("SELECT new org.example.rgybackend.Model.EmotionDataModel(e.timestamp, e.score, e.tagid, null) FROM Emotion e WHERE e.userid = :userid AND e.timestamp >= :start AND e.timestamp < :end")
    List<EmotionDataModel> scanEmotionData(String userid, Long start, Long end);

    @Query("SELECT new org.example.rgybackend.Model.EmotionDataModel(e.timestamp, e.score, e.tagid, null) FROM Emotion e WHERE e.timestamp >= :start AND e.timestamp < :end")
    List<EmotionDataModel> scanData(Long start, Long end);
}
