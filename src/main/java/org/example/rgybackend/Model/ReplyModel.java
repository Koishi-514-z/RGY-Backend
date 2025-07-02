package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyModel {
    private Long replyid;
    private Long blogid;
    private  SimplifiedProfileModel user;
    private Long timestamp;
    private String content;
}
