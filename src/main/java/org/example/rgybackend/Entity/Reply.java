package org.example.rgybackend.Entity;

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
    @Column(name = "userid")
    private Long blogid;

    @Basic
    @Column(name = "userid")
    private String fromuserid;

    @Basic
    @Column(name = "userid")
    private String touserid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "content")
    private String content;
}
