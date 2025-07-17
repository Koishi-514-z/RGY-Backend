package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.CrisisAuditingModel;

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
@Table(name = "crisis_auditing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrisisAuditing {
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
    @Column(name = "contentid")
    private Long contentid;

    @Basic
    @Column(name = "type")
    private Long type;

    @Basic
    @Column(name = "urgencyLevel")
    private Long urgencyLevel;


  
    public CrisisAuditing(CrisisAuditingModel model) {
        this.crisisid = model.getCrisisid();
        this.userid = model.getUserid();
        this.content = model.getContent();
        this.timestamp = model.getTimestamp();
        this.contentid = model.getContentid();
        this.type = model.getType();
        this.urgencyLevel = model.getUrgencyLevel();
    }

}
