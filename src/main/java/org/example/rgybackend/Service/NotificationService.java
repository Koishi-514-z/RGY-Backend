package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.NotificationPrivateModel;

public interface NotificationService {
    List<NotificationPrivateModel> getPrivateNotification(String userid);

    boolean addPrivateNotification(NotificationPrivateModel notification);

    boolean markRead(Long notificationid);

    boolean markAllRead(String userid);

    boolean deleteNotification(Long notificationid);
}
