package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {
    private Long messageid;
    private Long role;
    private Long timestamp;
    private String content;

    public MessageModel(Message message, Long role) {
        this.messageid = message.getMessageid();
        this.role = role;
        this.timestamp = message.getTimestamp();
        this.content = message.getContent();
    }
}
