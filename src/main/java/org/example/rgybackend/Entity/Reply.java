package org.example.rgybackend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.rgybackend.Model.BlogModel;
import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.ReplyModel;

@Entity
@Table(name = "reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @Column(name = "replyid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long replyid;

    @Basic
    @Column(name = "blogid")
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
    @Column(name = "content")
    @Getter
    @Setter
    private String content;
    @Basic
    @Column(name = "valid")
    @Getter
    @Setter
    private Integer valid;


    public Reply(ReplyModel replyModel) {
        this.replyid = null;
        this.blogid = replyModel.getBlogid();
        this.userid = replyModel.getUser().getUserid();
        this.timestamp = replyModel.getTimestamp();
        this.content = replyModel.getContent();
        this.valid = 1;
    }
}

