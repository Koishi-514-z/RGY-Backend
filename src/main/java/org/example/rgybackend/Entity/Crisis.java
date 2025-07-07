package org.example.rgybackend.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.rgybackend.Model.BlogModel;

@Entity
@Table(name = "crisis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crisis {
    @Id
    @Column(name = "crisisid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int crisisid;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "userid")
    private String userid;

    public Crisis(String content, Long timestamp, String userid) {
        this.content = content;
        this.timestamp = timestamp;
        this.userid = userid;
    }

}
