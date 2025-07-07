package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Crisis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrisisModel {
    private Long crisisid;
    private String userid;
    private SimplifiedProfileModel user;
    private String content;
    private Long timestamp;
    private Long urgencyLevel;
    private Long status;

    public CrisisModel(Crisis crisis) {
        this.crisisid = crisis.getCrisisid();
        this.userid = crisis.getUserid();
        this.content = crisis.getContent();
        this.timestamp = crisis.getTimestamp();
        this.urgencyLevel = crisis.getUrgencyLevel();
        this.status = crisis.getStatus();
    }
}
