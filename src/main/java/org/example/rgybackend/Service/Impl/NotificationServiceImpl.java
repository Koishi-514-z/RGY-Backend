package org.example.rgybackend.Service.Impl;

import java.util.List;

import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationPrivateDAO notificationPrivateDAO;

    @Override
    public List<NotificationPrivateModel> getPrivateNotification(String userid) {
        return notificationPrivateDAO.getUserNotification(userid);
    }

    @Override
    public boolean addPrivateNotification(NotificationPrivateModel notification) {
        return notificationPrivateDAO.addNotification(notification);
    }
}
