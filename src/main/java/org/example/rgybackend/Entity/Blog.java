package org.example.rgybackend.Entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    private Long blogid;
    private String userid;
    private Long timestamp;
    private Long likeNum;
    private String title;
    private String cover;
    private String content;
    private List<String> tags;
    private List<Reply> replies;
}
