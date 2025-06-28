package org.example.rgybackend.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionModel {
    private Long sessionid;
    private SimplifiedProfileModel myself;
    private SimplifiedProfileModel other;
    private Long timestamp;
    private Long unread;
    private List<MessageModel> messages;
}
