package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.CrisisAuditing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrisisAuditingModel {
    private Integer crisisid;
    private String userid;
    private String content;
    private Long timestamp;
    private Long contentid;
    private Long type;
    private Long urgencyLevel;
}
