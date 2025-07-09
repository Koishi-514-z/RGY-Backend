package org.example.rgybackend.DAO;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.DTO.DiaryLabelData;
import org.example.rgybackend.Model.DiaryModel;

public interface DiaryDAO {
    boolean diaryExists(String userid, LocalDate date);

    DiaryModel getDiary(String userid, LocalDate date);

    List<DiaryModel> scanDiary(String userid, LocalDate startDate, LocalDate endDate);

    List<DiaryLabelData> scanLabel(String userid, LocalDate startDate, LocalDate endDate);

    boolean setDiary(DiaryModel diaryModel);
}
