package org.example.rgybackend.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.MessageModel;
import org.example.rgybackend.Model.SessionModel;
import org.example.rgybackend.Model.SessionTagModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    
    @GetMapping("/getsession")
    public SessionModel getSession(@RequestParam String sessionid, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return chatService.getSession(userid, sessionid);
    }

    @GetMapping("/gettags")
    public List<SessionTagModel> getSessionTags(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return chatService.getSessionTags(userid);
    }

    @GetMapping("/getid")
    public Long getSessionid(@RequestParam String userid, HttpSession session) {
        String fromuserid = (String)session.getAttribute("user");
        return chatService.getSessionid(fromuserid, userid);
    }
    
    @PutMapping("/post")
    public boolean postMessage(@RequestParam String sessionid, @RequestBody StringDTO content) {
        return chatService.postMessage(sessionid, content);
    }

    @PutMapping("/read")
    public boolean updateRead(@RequestParam String sessionid) {
        return chatService.updateRead(sessionid);
    }

    @PostMapping("/post")
    public Long createSession(@RequestParam String userid, HttpSession session) {
        String fromuserid = (String)session.getAttribute("user");
        return chatService.createSession(fromuserid, userid);
    }
}
