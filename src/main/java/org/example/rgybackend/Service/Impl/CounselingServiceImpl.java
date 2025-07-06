package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.DAO.AvailableDAO;
import org.example.rgybackend.DAO.CounselingDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.Entity.Counseling;
import org.example.rgybackend.Model.AvailableTimeModel;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Service.CounselingService;
import org.example.rgybackend.Utils.NotificationUtil;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounselingServiceImpl implements CounselingService {
    @Autowired
    private CounselingDAO counselingDAO;

    @Autowired
    private AvailableDAO availableDAO;

    @Autowired
    private NotificationPrivateDAO notificationPrivateDAO;

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
        counselingModel.setPsyid(psyid);
        counselingModel.setTimestamp(timestamp);
        counselingModel.setStatus(0L);
        return counselingDAO.addCounseling(counselingModel, userid);
    }

    // 0-->pending, 1-->accepted, 2-->finished, 3-->rejected
    @Override
    public boolean setStatus(Long counselingid, Long status) {      
        Counseling counseling = counselingDAO.getCounselingById(counselingid);
        if(status == 1) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.counselingAccepted);
            notification.setAdminid(counseling.getPsyid());
            notification.setUserid(counseling.getUserid());
            notificationPrivateDAO.addNotification(notification);
        }
        else if(status == 2) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.counselingFinished);
            notification.setAdminid(counseling.getPsyid());
            notification.setUserid(counseling.getUserid());
            notificationPrivateDAO.addNotification(notification);
        }
        else if(status == 3) {
            NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.counselingRejected);
            notification.setAdminid(counseling.getPsyid());
            notification.setUserid(counseling.getUserid());
            notificationPrivateDAO.addNotification(notification);
        }
        return counselingDAO.setStatus(counselingid, status);
    }

    @Override
    public boolean removeCounseling(Long counselingid) {
        return counselingDAO.remove(counselingid);
    }

    @Override
    public AvailableTimeModel getAvailableTime(String psyid) {
        return availableDAO.getAvailableTime(psyid);
    }

    @Override
    public boolean setAvailableTimes(AvailableTimeModel availableTimeModel) {
        return availableDAO.setAvailableTimes(availableTimeModel);
    }
}
