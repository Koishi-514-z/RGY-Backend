package org.example.rgybackend.Entity;

import java.util.Date;

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
    private Date timestamp;
    private String content;
}
