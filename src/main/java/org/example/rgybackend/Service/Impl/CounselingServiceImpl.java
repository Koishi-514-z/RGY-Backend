package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.DAO.AvailableDAO;
import org.example.rgybackend.DAO.CounselingDAO;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Service.CounselingService;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounselingServiceImpl implements CounselingService {
    @Autowired
    private CounselingDAO counselingDAO;

    @Autowired
    private AvailableDAO availableDAO;

    @Override
    public List<CounselingModel> getCounseling(String psyid) {
        return counselingDAO.getCounseling(psyid);
    }

    @Override
    public List<CounselingModel> getDateCounseling(String psyid, Long timestamp) {
        LocalDate date = TimeUtil.getLocalDate(timestamp);
        return counselingDAO.getDateCounseling(psyid, date);
    }

    @Override
    public boolean addCounseling(String userid, String psyid, Long timestamp) {
        CounselingModel counselingModel = new CounselingModel();
        counselingModel.setUserid(userid);
        counselingModel.setPsyid(psyid);
        counselingModel.setTimestamp(timestamp);
        return counselingDAO.addCounseling(counselingModel);
    }

    @Override
    public boolean removeCounseling(Long counselingid) {
        return counselingDAO.remove(counselingid);
    }

    @Override
    public List<Long> getAvailableTime(String psyid) {
        return availableDAO.getAvailableTime(psyid);
    }

    @Override
    public boolean setAvailableTimes(String psyid, List<Long> timestamps) {
        return availableDAO.setAvailableTimes(psyid, timestamps);
    }

    @Override
    public boolean addAvailableTime(String psyid, Long timestamp) {
        return availableDAO.addAvailableTime(psyid, timestamp);
    }

    @Override
    public boolean removeAvailableTime(String psyid, Long timestamp) {
        return availableDAO.removeAvailableTime(psyid, timestamp);
    }
}
