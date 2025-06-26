package org.example.rgybackend.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.Message;
import org.example.rgybackend.Model.Session;
import org.example.rgybackend.Model.SessionTag;
import org.example.rgybackend.Model.SimplifiedProfile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @GetMapping("/getsession")
    public Session getSession(@RequestParam String sessionid) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new Message(2L, 1L, 9851296581L, "Koishi_Plus"));
        messages.add(new Message(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new Message(2L, 1L, 9851296581L, "Koishi_Plus"));
        messages.add(new Message(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new Message(2L, 1L, 9851296581L, "Koishi_Plus"));
        messages.add(new Message(1L, 0L, 9851296581L, "Koishi"));
        messages.add(new Message(2L, 1L, 9851296581L, "Koishi_Plus"));
        return new Session(1L, new SimplifiedProfile("123456789", "Koishi", null, null), new SimplifiedProfile("12345678", "Koishi_Plus", null, null), 6871685741L, messages);
    }

    @GetMapping("/gettags")
    public List<SessionTag> getSessionTags() {
        List<SessionTag> tags = new ArrayList<>();
        tags.add(new SessionTag(1L, new SimplifiedProfile("123456789", "Koishi", null, null), new SimplifiedProfile("12345678", "Koishi_Plus", null, null), 6871685741L));
        tags.add(new SessionTag(2L, new SimplifiedProfile("123456789", "Koishi", null, null), new SimplifiedProfile("12345678", "Koishi_Plus", null, null), 6871685741L));
        tags.add(new SessionTag(3L, new SimplifiedProfile("123456789", "Koishi", null, null), new SimplifiedProfile("12345678", "Koishi_Plus", null, null), 6871685741L));
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

    @PostMapping("/post")
    public Long createSession(@RequestParam String userid) {
        return 1L;
    }
}
