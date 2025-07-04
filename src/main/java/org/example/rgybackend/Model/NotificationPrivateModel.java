package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.NotificationPrivate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPrivateModel {
    private Long notificationid;
    private Long type;
    private String adminid;
    private String userid;
    private String title;
    private String content;
    private Long timestamp;
    private Long unread;
    private String priority;

    public NotificationPrivateModel(NotificationPrivate notificationPrivate) {
        this.notificationid = notificationPrivate.getNotificationid();
        this.type = notificationPrivate.getType();
        this.adminid = notificationPrivate.getAdminid();
        this.userid = notificationPrivate.getUserid();
        this.title = notificationPrivate.getTitle();
        this.content = notificationPrivate.getContent();
        this.timestamp = notificationPrivate.getTimestamp();
        this.unread = notificationPrivate.getUnread();
        this.priority = notificationPrivate.getPriority();
    }

    public NotificationPrivateModel(NotificationPrivateModel other) {
        this.notificationid = other.notificationid;
        this.type = other.type;
        this.adminid = other.adminid;
        this.userid = other.userid;
        this.title = other.title;
        this.content = other.content;
        this.timestamp = other.timestamp;
        this.unread = other.unread;
        this.priority = other.priority;
    }
}
