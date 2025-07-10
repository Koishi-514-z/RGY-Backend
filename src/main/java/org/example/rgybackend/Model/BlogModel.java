package org.example.rgybackend.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogModel {
    private Long blogid;
    private SimplifiedProfileModel user;
    private Long timestamp;
    private Long likeNum;
    private String title;
    private String content;
    private List<String> tags;
    private List<ReplyModel> replies;
    private int emotion;
    private Long lastreply;
    private Long browsenum;
    private int valid;
}
