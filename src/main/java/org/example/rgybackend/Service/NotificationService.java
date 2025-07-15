package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Model.NotificationSentModel;

public interface NotificationService {
    List<NotificationPrivateModel> getPrivateNotification(String userid);

    boolean addPrivateNotification(NotificationPrivateModel notification);

    boolean markRead(Long notificationid);

    boolean markAllPrivateRead(String userid);

    boolean markAllPublicRead(String userid);

    boolean deleteNotification(Long notificationid);

    List<NotificationSentModel> getPrivateNotificationMine(String userid);
}
