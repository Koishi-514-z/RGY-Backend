package org.example.rgybackend.DAO;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Model.DiaryModel;

public interface DiaryDAO {
    List<DiaryModel> scanDiary(String userid, LocalDate startDate, LocalDate endDate);

    boolean setDiary(DiaryModel diaryModel);
}
