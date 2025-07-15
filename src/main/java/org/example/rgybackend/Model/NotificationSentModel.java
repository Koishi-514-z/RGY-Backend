package org.example.rgybackend.Model;
import org.example.rgybackend.Entity.NotificationPrivate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSentModel {

    private Long type;
    private String adminid;
    private String title;
    private String content;
    private Long timestamp;
    private Long unreadnum;
    private String priority;

}
