package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.Entity.NotificationPrivate;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Repository.NotificationPrivateRepository;
import org.example.rgybackend.Utils.NotExistException;
import org.example.rgybackend.Utils.Notification;
import org.example.rgybackend.Utils.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationPrivateDAOImpl implements NotificationPrivateDAO {
    @Autowired
    private NotificationPrivateRepository notificationPrivateRepository;

    @Autowired
    private Notification socket;

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
        SocketMessage sockMessage = new SocketMessage("System", notification.getNotificationid(), notification.getAdminid(), notification.getUserid(), notification.getTimestamp(), notification.getContent());
        socket.pushNotificationToUser(sockMessage);
        return true;
    }

    @Override
    public boolean markRead(Long notificationid) {
        Optional<NotificationPrivate> notificationOptional = notificationPrivateRepository.findById(notificationid);
        if(notificationOptional.isEmpty()) {
            throw new NotExistException("Notification not exists, notificationid: " + notificationid);
        }
        NotificationPrivate notificationPrivate = notificationOptional.get();
        notificationPrivate.setUnread(0L);
        notificationPrivateRepository.save(notificationPrivate);
        return true;
    }

    @Override
    public boolean markAllRead(String userid) {
        List<NotificationPrivate> notificationPrivates = notificationPrivateRepository.findByUserid(userid);
        for(NotificationPrivate notificationPrivate : notificationPrivates) {
            notificationPrivate.setUnread(0L);
            notificationPrivateRepository.save(notificationPrivate);
        }
        return true;
    }

    @Override
    public boolean deleteNotification(Long notificationid) {
        notificationPrivateRepository.deleteById(notificationid);
        return true;
    }
}
