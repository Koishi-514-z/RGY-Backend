package org.example.rgybackend.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rgybackend.DTO.ProfileTag;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedReplyModel {
    private Long replyid;
    private Long blogid;
    private String fromuserid;
    private String touserid;
    private Long timestamp;
    private String content;
    private ProfileTag user;
}
