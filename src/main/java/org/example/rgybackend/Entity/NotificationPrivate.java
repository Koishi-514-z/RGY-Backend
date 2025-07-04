package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.NotificationPrivateModel;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_private")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPrivate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "notificationid")
    private Long notificationid;

    @Basic
    @Column(name = "type")
    private Long type;

    @Basic
    @Column(name = "adminid")
    private String adminid;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "title")
    private String title;

     @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "unread")
    private Long unread;

    @Basic
    @Column(name = "priority")
    private String priority;

    public NotificationPrivate(NotificationPrivateModel notificationPrivateModel) {
        this.notificationid = notificationPrivateModel.getNotificationid();
        this.type = notificationPrivateModel.getType();
        this.adminid = notificationPrivateModel.getAdminid();
        this.userid = notificationPrivateModel.getUserid();
        this.title = notificationPrivateModel.getTitle();
        this.content = notificationPrivateModel.getContent();
        this.timestamp = notificationPrivateModel.getTimestamp();
        this.unread = notificationPrivateModel.getUnread();
        this.priority = notificationPrivateModel.getPriority();
    }
}
