package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.Entity.NotificationPrivate;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Repository.NotificationPrivateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationPrivateDAOImpl implements NotificationPrivateDAO {
    @Autowired
    private NotificationPrivateRepository notificationPrivateRepository;

    @Override
    public List<NotificationPrivateModel> getUserNotification(String userid) {
        List<NotificationPrivate> notificationPrivates = notificationPrivateRepository.findByUserid(userid);
        notificationPrivates.sort((s1, s2) -> s2.getTimestamp().compareTo(s1.getTimestamp()));
        
        List<NotificationPrivateModel> notificationPrivateModels = new ArrayList<>();
        for(NotificationPrivate notificationPrivate : notificationPrivates) {
            notificationPrivateModels.add(new NotificationPrivateModel(notificationPrivate));
        }
        return notificationPrivateModels;
    }

    @Override
    public boolean addNotification(NotificationPrivateModel notification) {
        NotificationPrivate notificationPrivate = new NotificationPrivate(notification);
        notificationPrivateRepository.save(notificationPrivate);
        return true;
    }
}
