package org.example.rgybackend.Model;

import java.util.List;

import org.example.rgybackend.DTO.LikeData;
import org.example.rgybackend.DTO.ReplyData;
import org.example.rgybackend.Entity.Blog;
import org.example.rgybackend.Entity.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileModel {
    private String userid;
    private String username;
    private String email;
    private String avatar;
    private String note;
    private Long role;
    private Long jointime;
    private Long level;

    public ProfileModel(UserProfile userProfile) {
        this.userid = userProfile.getUserid();
        this.username = userProfile.getUsername();
        this.email = userProfile.getEmail();
        this.avatar = userProfile.getAvatar();
        this.note = userProfile.getNote();
        this.role = userProfile.getRole();
        this.jointime = userProfile.getJointime();
        this.level = 0L;
    }

    public void calcLevel(List<Blog> blogs, List<ReplyData> replyDatas, List<LikeData> likeDatas) {
        final double blogWeight = 8.0, replyWeight = 1.0, likeWeight = 0.2;
        double blogScore = 0, replyScore = 0, likeScore = 0;

        for(Blog blog : blogs) {
            blogScore += (blogWeight * (1 + blog.getBrowsenum() / 100));
        }
        replyScore = replyWeight * replyDatas.size();
        likeScore = likeWeight * likeDatas.size();

        this.level = (long) (blogScore + replyScore + likeScore) / 10;
    }
}
