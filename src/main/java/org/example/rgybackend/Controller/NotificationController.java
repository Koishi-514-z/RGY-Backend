package org.example.rgybackend.Controller;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;
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

    @GetMapping("/get")
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

    @PutMapping("/multiple/add")
    public boolean addMultiplePrivateNotification(@RequestBody String params, HttpSession session) {
        String adminid = (String)session.getAttribute("user");
        Long timestamp = TimeUtil.now();
        JSONObject json = JSONObject.parseObject(params);
        List<String> users = json.getJSONArray("users").toJavaList(String.class);
        Long type = json.getLong("type");
        String title = json.getString("title");
        String content = json.getString("content");
        String priority = json.getString("priority");
        for (String userid : users) {
            NotificationPrivateModel notification = new NotificationPrivateModel();
            notification.setUserid(userid);
            notification.setAdminid(adminid);
            notification.setTimestamp(timestamp);
            notification.setType(type);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setPriority(priority);
            notification.setUnread(1L);
            notificationService.addPrivateNotification(notification);
        }
        return true;

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
