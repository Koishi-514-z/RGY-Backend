package org.example.rgybackend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.ProfileModel;

@Entity
@Table(name = "blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @Column(name = "blogid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long blogid;

    @Basic
    @Column(name = "userid")
    @Getter
    @Setter
    private String userid;

    @Basic
    @Column(name = "timestamp")
    @Getter
    @Setter
    private Long timestamp;

    @Basic
    @Column(name = "likeNum")
    @Getter
    @Setter
    private Long likeNum;

    @Basic
    @Column(name = "title")
    @Getter
    @Setter
    private String title;

    @Basic
    @Column(name = "content")
    @Getter
    @Setter
    private String content;

    @Basic
    @Column(name = "tags")
    @Getter
    @Setter
    private String tags;

    @Basic
    @Column(name = "valid")
    @Getter
    @Setter
    private Integer valid;
    @Basic
    @Column(name = "emotion")
    @Getter
    @Setter
    private int emotion;
    public Blog(BlogModel blogModel) {
        this.userid = blogModel.getUser().getUserid();
        this.timestamp = blogModel.getTimestamp();
        this.likeNum = blogModel.getLikeNum();
        this.title = blogModel.getTitle();
        this.content = blogModel.getContent();
        this.blogid = blogModel.getBlogid();
        for (String tag : blogModel.getTags()) {
            this.tags += tag + ",";
        }
        this.tags = this.tags.substring(0, this.tags.length() - 1);
        this.valid = 1;
        this.emotion = blogModel.getEmotion();
    }
}
