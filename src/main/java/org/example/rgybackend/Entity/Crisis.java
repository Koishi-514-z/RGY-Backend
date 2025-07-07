package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.CrisisModel;

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
@Table(name = "crisis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crisis {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "crisisid")
    private Integer crisisid;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;
  
    @Basic
    @Column(name = "urgencyLevel")
    private Long urgencyLevel;

    @Basic
    @Column(name = "status")
    private Long status;

    public Crisis(String content, Long timestamp, String userid) {
        this.content = content;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public Crisis(CrisisModel model) {
        this.crisisid = model.getCrisisid();
        this.userid = model.getUserid();
        this.content = model.getContent();
        this.timestamp = model.getTimestamp();
        this.urgencyLevel = model.getUrgencyLevel();
        this.status = model.getStatus();
    }
}
