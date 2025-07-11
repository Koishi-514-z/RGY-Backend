package org.example.rgybackend.Service.Impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.AvailableDAO;
import org.example.rgybackend.DAO.CounselingDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.DAO.PsyExtraDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.DTO.PsyCommentData;
import org.example.rgybackend.Entity.Counseling;
import org.example.rgybackend.Model.AvailableTimeModel;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Service.CounselingService;
import org.example.rgybackend.Utils.CacheUtil;
import org.example.rgybackend.Utils.NotificationUtil;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CounselingServiceImpl implements CounselingService {
    @Autowired
    private CounselingDAO counselingDAO;

    @Autowired
    private AvailableDAO availableDAO;

    @Autowired
    private NotificationPrivateDAO notificationPrivateDAO;

    @Autowired
    private PsyExtraDAO psyExtraDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CacheUtil cacheUtil;

    @Override
    public List<CounselingModel> getCounseling(String psyid) {
        return counselingDAO.getCounseling(psyid);
    }

    @Override
    public List<CounselingModel> getUserCounseling(String userid) {
        return counselingDAO.getUserCounseling(userid);
    }

    @Override
    public List<CounselingModel> getDateCounseling(String psyid, Long timestamp) {
        LocalDate date = TimeUtil.getLocalDate(timestamp);
        return counselingDAO.getDateCounseling(psyid, date);
    }

    @Override
    public boolean addCounseling(CounselingModel counselingModel, String userid) {
        NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.counselingNewOrder);
        notification.setAdminid("System");
        notification.setUserid(counselingModel.getPsyid());
        notificationPrivateDAO.addNotification(notification);
        return counselingDAO.addCounseling(counselingModel, userid);
    }

    @Override
    @CacheEvict(value = "psyprofile", key = "#psyCommentData.psyid")
    public boolean addComment(PsyCommentData psyCommentData) {
        return psyExtraDAO.addComments(psyCommentData);
    }

    // 0-->pending, 1-->accepted, 2-->finished, 3-->rejected
    @Override
    public boolean setStatus(Long counselingid, Long status, String userid) {      
        Counseling counseling = counselingDAO.getCounselingById(counselingid);
        Long role = userDAO.getRole(userid);
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
            psyExtraDAO.increaseClients(counseling.getPsyid());
            cacheUtil.evictPsyProfileCache(counseling.getPsyid());
        }
        else if(status == 3) {
            if(role == 0) {
                NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.counselingCanceled);
                notification.setAdminid("System");
                notification.setUserid(counseling.getPsyid());
                notificationPrivateDAO.addNotification(notification);
            }
            else if(role == 2) {
                NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.counselingRejected);
                notification.setAdminid(counseling.getPsyid());
                notification.setUserid(counseling.getUserid());
                notificationPrivateDAO.addNotification(notification);
            }
            counselingDAO.removeCounseling(counselingid);
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
    public List<Long> getDateAvailables(String psyid, Long timestamp) {
        AvailableTimeModel availableTimeModel = availableDAO.getAvailableTime(psyid);
        LocalDate date = TimeUtil.getLocalDate(timestamp);
        List<Long> workingslots = new ArrayList<>();
        List<Long> dateAvailables = new ArrayList<>();

        if(date.getDayOfWeek() == DayOfWeek.MONDAY) {
            workingslots = availableTimeModel.getMonday();
        } 
        else if(date.getDayOfWeek() == DayOfWeek.TUESDAY) {
            workingslots = availableTimeModel.getTuesday();
        } 
        else if(date.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
            workingslots = availableTimeModel.getWednesday();
        } 
        else if(date.getDayOfWeek() == DayOfWeek.THURSDAY) {
            workingslots = availableTimeModel.getThursday();
        } 
        else if(date.getDayOfWeek() == DayOfWeek.FRIDAY) {
            workingslots = availableTimeModel.getFriday();
        } 
        else if(date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            workingslots = availableTimeModel.getSaturday();
        } 
        else if(date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            workingslots = availableTimeModel.getSunday();
        }

        for(Long slot : workingslots) {
            LocalDateTime ldt = TimeUtil.getLocalDateTime(date, slot);
            boolean counseled = counselingDAO.counseled(psyid, TimeUtil.getTimestamp(ldt));
            if(!counseled) {
                dateAvailables.add(slot);
            }
        }
        return dateAvailables;
    }

    @Override
    public boolean setAvailableTimes(AvailableTimeModel availableTimeModel) {
        return availableDAO.setAvailableTimes(availableTimeModel);
    }

    @Override
    public List<TagModel> getTypeTags() {
        return new CounselingModel().typeTags;
    }
}
