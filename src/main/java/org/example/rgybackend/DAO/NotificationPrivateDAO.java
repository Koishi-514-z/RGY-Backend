package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Model.NotificationSentModel;

public interface NotificationPrivateDAO {
    List<NotificationPrivateModel> getUserNotification(String userid);

    boolean addNotification(NotificationPrivateModel notification);

    boolean markRead(Long notificationid);

    boolean markAllPrivateRead(String userid);

    boolean markAllPublicRead(String userid);

    boolean deleteNotification(Long notificationid);

    List<NotificationSentModel> getNotificationSent(String userid);
}
