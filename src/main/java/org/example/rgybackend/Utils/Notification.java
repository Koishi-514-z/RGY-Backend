package org.example.rgybackend.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Notification {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void pushBlogToAllClients(SocketMessage message) {
        messagingTemplate.convertAndSend("/topic/messages/blog", message);
    }

    public void pushChatToUser(SocketMessage message) {
        messagingTemplate.convertAndSendToUser(message.getTouserid(), "/queue/notifications/chat", message);
    }

    public void pushNotificationToUser(SocketMessage message) {
        messagingTemplate.convertAndSendToUser(message.getTouserid(), "/queue/notifications/notify", message);
    }
}
