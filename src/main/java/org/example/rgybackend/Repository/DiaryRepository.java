package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUserid(String userid);

    @Query("SELECT new org.example.rgybackend.Entity.Diary(d.diaryid, d.userid, d.timestamp, d.label, d.content) FROM Diary d WHERE d.userid = :userid AND d.timestamp >= :start AND d.timestamp < :end")
    List<Diary> scanDiary(String userid, Long start, Long end);

    @Query("SELECT new Long(d.label) FROM Diary d WHERE d.userid = :userid AND d.timestamp >= :start AND d.timestamp < :end")
    List<Long> scanEmotionLabel(String userid, Long start, Long end);
}
