package org.example.rgybackend.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rgybackend.DTO.ProfileTag;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedBlogModel {
    private Long blogid;
    private ProfileTag user;
    private Long timestamp;
    private Long likeNum;
    private String title;
    private String content;
    private List<String> tags;
    private List<SimplifiedReplyModel> replies;
    private int emotion;
    private Long lastreply;
    private Long browsenum;
    private int valid;
}
