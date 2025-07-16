package org.example.rgybackend.DAO.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.DiaryDAO;
import org.example.rgybackend.Entity.Diary;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Repository.DiaryRepository;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DiaryDAOImpl implements DiaryDAO {
    @Autowired
    private DiaryRepository diaryRepository;

    @Override
    public List<DiaryModel> scanDiary(String userid, LocalDate startDate, LocalDate endDate) {
        Long start = TimeUtil.getStartOfDayTimestamp(startDate);
        Long end = TimeUtil.getStartOfDayTimestamp(endDate) + TimeUtil.DAY;
        List<Diary> diaries = diaryRepository.scanDiary(userid, start, end);
        List<DiaryModel> diaryModels = new ArrayList<>();
        for(Diary diary : diaries) {
            diaryModels.add(new DiaryModel(diary));
        }
        return diaryModels;
    }

    @Override
    public boolean setDiary(DiaryModel diaryModel) {
        Long timestamp = TimeUtil.getStartOfDayTimestamp(diaryModel.getTimestamp());
        List<Diary> diaries = diaryRepository.scanDiary(diaryModel.getUserid(), timestamp, timestamp + TimeUtil.DAY);
        if(diaries.isEmpty()) {
            Diary diary = new Diary(diaryModel);
            diaryRepository.save(diary);
        }
        else {
            Diary oldDiary = diaries.get(0);
            Diary newDiary = new Diary(diaryModel);
            newDiary.setDiaryid(oldDiary.getDiaryid());
            diaryRepository.save(newDiary);
        }
        return true;
    }
}
