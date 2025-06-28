package org.example.rgybackend.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.MessageModel;
import org.example.rgybackend.Model.SessionModel;
import org.example.rgybackend.Model.SessionTagModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @GetMapping("/getsession")
    public SessionModel getSession(@RequestParam String sessionid) {
        List<MessageModel> messages = new ArrayList<>();
        messages.add(new MessageModel(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new MessageModel(2L, 1L, 9851296581L, "Koishi_Plus"));
        messages.add(new MessageModel(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new MessageModel(2L, 1L, 9851296581L, "Koishi_Plus"));
        messages.add(new MessageModel(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new MessageModel(2L, 1L, 9851296581L, "Koishi_Plus"));
        messages.add(new MessageModel(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new MessageModel(2L, 1L, 9851296581L, "Koishi_Plus"));
        return new SessionModel(1L, new SimplifiedProfileModel("123456789", "Koishi", null, null), new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null), 6871685741L, 1L, messages);
    }

    @GetMapping("/gettags")
    public List<SessionTagModel> getSessionTags() {
        List<SessionTagModel> tags = new ArrayList<>();
        tags.add(new SessionTagModel(1L, new SimplifiedProfileModel("123456789", "Koishi", null, null), new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null), 6871685741L, 1L));
        tags.add(new SessionTagModel(2L, new SimplifiedProfileModel("123456789", "Koishi", null, null), new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null), 6871685741L, 0L));
        tags.add(new SessionTagModel(3L, new SimplifiedProfileModel("123456789", "Koishi", null, null), new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null), 6871685741L, 2L));
        return tags;
    }

    @GetMapping("/getid")
    public Long getSessionid(@RequestParam String userid) {
        return 1L;
    }
    
    @PutMapping("/post")
    public boolean postMessage(@RequestParam String sessionid, @RequestBody StringDTO content) {
        return true;
    }

    @PutMapping("/read")
    public boolean updateRead(@RequestParam String sessionid) {
        return true;
    }

    @PostMapping("/post")
    public Long createSession(@RequestParam String userid) {
        return 1L;
    }
}
