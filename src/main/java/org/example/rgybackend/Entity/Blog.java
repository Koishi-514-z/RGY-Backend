package org.example.rgybackend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.rgybackend.Model.BlogModel;

@Entity
@Table(name = "blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @Column(name = "blogid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogid;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "likeNum")
    private Long likeNum;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "tags")
    private String tags;

    @Basic
    @Column(name = "valid")
    private Integer valid;

    @Basic
    @Column(name = "emotion")
    private int emotion;

    @Basic
    @Column(name = "lastreply")
    private Long lastreply;

    @Basic
    @Column(name = "browsenum")
    private Long browsenum;
    public Blog(BlogModel blogModel) {
        this.userid = blogModel.getUserid();
        this.timestamp = blogModel.getTimestamp();
        this.likeNum = blogModel.getLikeNum();
        this.title = blogModel.getTitle();
        this.content = blogModel.getContent();
        this.blogid = blogModel.getBlogid();
        this.tags = "";
        for (String tag : blogModel.getTags()) {
            this.tags += tag + ",";
        }
        this.tags = this.tags.substring(0, this.tags.length() - 1);
        this.valid = 1;
        this.emotion = blogModel.getEmotion();
        this.lastreply = blogModel.getTimestamp();
        this.browsenum = 0L;
    }


    public Blog(BlogModel blogModel,int valid) {
        this.userid = blogModel.getUserid();
        this.timestamp = blogModel.getTimestamp();
        this.likeNum = blogModel.getLikeNum();
        this.title = blogModel.getTitle();
        this.content = blogModel.getContent();
        this.blogid = blogModel.getBlogid();
        this.tags = "";
        for (String tag : blogModel.getTags()) {
            this.tags += tag + ",";
        }
        this.tags = this.tags.substring(0, this.tags.length() - 1);
        this.valid = valid;
        this.emotion = blogModel.getEmotion();
        this.lastreply = blogModel.getTimestamp();
        this.browsenum = 0L;
    }
}
