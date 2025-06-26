package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private Long replyid;
    private Long blogid;
    private String userid;
    private Long timestamp;
    private String content;
}
