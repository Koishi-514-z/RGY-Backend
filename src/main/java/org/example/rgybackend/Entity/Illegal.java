package org.example.rgybackend.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.rgybackend.Model.BlogModel;

@Entity
@Table(name = "illegal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Illegal {
    @Id
    @Column(name = "illegalid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int illegalid;
    @Basic
    @Column(name = "type")
    private int type;

    @Basic
    @Column(name = "contentid")
    private Long contentid;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "reason")
    private String reason;

    @Basic
    @Column(name = "status")
    private int status;

    public Illegal(int type, Long contentid, String userid, Long timestamp, String reason, int status) {
        this.type = type;
        this.contentid = contentid;
        this.userid = userid;
        this.timestamp = timestamp;
        this.reason = reason;
        this.status = status;
    }



}
