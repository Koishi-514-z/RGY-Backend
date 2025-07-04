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
@Table(name = "user_like")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "likeid")
    private Long likeid;

    @Basic
    @Column(name = "fromuserid")
    private String fromuserid;

    @Basic
    @Column(name = "touserid")
    private String touserid;

    @Basic
    @Column(name = "blogid")
    private Long blogid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    public Like(String fromuserid, String touserid, Long blogid, Long timestamp) {
        this.likeid = null;
        this.fromuserid = fromuserid;
        this.touserid = touserid;
        this.blogid = blogid;
        this.timestamp = timestamp;
    }
}
