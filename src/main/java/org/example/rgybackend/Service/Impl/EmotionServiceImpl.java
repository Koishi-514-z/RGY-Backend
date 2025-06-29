package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.DAO.EmotionDAO;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Service.EmotionService;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmotionServiceImpl implements EmotionService {
    @Autowired
    private EmotionDAO emotionDAO;

    @Override
    public EmotionModel getEmotion(String userid) {
        return emotionDAO.getEmotion(userid, TimeUtil.today());
    }

    @Override
    public List<TagModel> getTags() {
        return emotionDAO.getTags();
    }

    @Override
    public List<UrlDataModel> getUrlDatas(String userid) {

    }

    @Override
    public List<UrlDataModel> getAllUrlDatas() {

    }

    @Override
    public boolean checkNegative(String userid) {

    }

    @Override
    public DiaryModel getDiary(String userid) {

    }

    @Override
    public List<EmotionDataModel> getWeekData(String userid) {
        return emotionDAO.getEmotionData(userid, TimeUtil.firstDayOfWeek(), TimeUtil.today());
    }

    @Override
    public List<EmotionDataModel> getMonthData(String userid) {
        return emotionDAO.getEmotionData(userid, TimeUtil.firstDayOfMonth(), TimeUtil.today());
    }

    @Override
    public boolean BookCounseling(Long timestamp) {

    }

    @Override
    public boolean setEmotion(EmotionModel emotionModel) {
        return emotionDAO.setEmotion(emotionModel);
    }

    @Override
    public boolean updateDiary(String userid, String content) {

    }
}
