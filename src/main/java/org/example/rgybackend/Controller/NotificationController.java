package org.example.rgybackend.Controller;

import java.util.List;

import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Service.NotificationService;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/private/get")
    public List<NotificationPrivateModel> getPrivateNotification(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return notificationService.getPrivateNotification(userid);
    }

    @PutMapping("/private/add")
    public boolean addPrivateNotification(@RequestBody NotificationPrivateModel notification, HttpSession session) {
        String adminid = (String)session.getAttribute("user");
        notification.setAdminid(adminid);
        notification.setTimestamp(TimeUtil.now());
        return notificationService.addPrivateNotification(notification);
    }

    @PostMapping("/private/markread")
    public boolean markRead(@RequestParam Long notificationid) {
        return notificationService.markRead(notificationid);
    }

    @PostMapping("/private/markallread")
    public boolean markAllRead(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return notificationService.markAllRead(userid);
    }

    @DeleteMapping("/private/del")
    public boolean deleteNotification(@RequestParam Long notificationid) {
        return notificationService.deleteNotification(notificationid);
    }
    
}
