package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Notification;
import org.example.rgybackend.Entity.NotificationPrivate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModel {
    private Long notificationid;
    private String adminid;
    private String title;
    private String content;
    private Long timestamp;
    private String priority;

    public NotificationModel(Notification notification) {
        this.notificationid = notification.getNotificationid();
        this.adminid = notification.getAdminid();
        this.title = notification.getTitle();
        this.content = notification.getContent();
        this.timestamp = notification.getTimestamp();
        this.priority = notification.getPriority();
    }

    public NotificationModel(NotificationModel other) {
        this.notificationid = other.notificationid;
        this.adminid = other.adminid;
        this.title = other.title;
        this.content = other.content;
        this.timestamp = other.timestamp;
        this.priority = other.priority;
    }
}
