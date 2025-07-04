package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Model.NotificationPrivateModel;

public interface NotificationPrivateDAO {
    List<NotificationPrivateModel> getUserNotification(String userid);

    boolean addNotification(NotificationPrivateModel notification);
}
