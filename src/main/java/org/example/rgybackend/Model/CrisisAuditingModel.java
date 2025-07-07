package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.CrisisAuditing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrisisAuditingModel {
    private Long crisisid;
    private String userid;
    private String content;
    private Long timestamp;

    public CrisisAuditingModel(CrisisAuditing entity) {
        this.crisisid = entity.getCrisisid();
        this.userid = entity.getUserid();
        this.content = entity.getContent();
        this.timestamp = entity.getTimestamp();
    }
    
    public CrisisAuditingModel(CrisisAuditingModel other) {
        this.crisisid = other.crisisid;
        this.userid = other.userid;
        this.content = other.content;
        this.timestamp = other.timestamp;
    }
}
