package org.example.rgybackend.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Notification {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void pushToAllClients(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    public void pushToUser(ChatMessage message) {
        messagingTemplate.convertAndSendToUser(message.getTouserid(), "/queue/notifications", message);
    }

}
