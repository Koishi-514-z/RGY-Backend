package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.ReplyModel;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "replyid")
    private Long replyid;

    @Basic
    @Column(name = "blogid")
    private Long blogid;

    @Basic
    @Column(name = "fromuserid")
    private String fromuserid;

    @Basic
    @Column(name = "touserid")
    private String touserid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "valid")
    private Integer valid;

    public Reply(ReplyModel replyModel,int valid) {
        this.replyid = replyModel.getReplyid();
        this.blogid = replyModel.getBlogid();
        this.fromuserid = replyModel.getFromuserid();
        this.touserid = replyModel.getTouserid();
        this.timestamp = replyModel.getTimestamp();
        this.content = replyModel.getContent();
        this.valid = valid;
    }
}
