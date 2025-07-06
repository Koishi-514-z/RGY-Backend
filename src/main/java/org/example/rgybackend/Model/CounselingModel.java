package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Counseling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselingModel {
    private Long counselingid;
    private String psyid;
    private Long timestamp;
    private Long status;
    private String type;

    public CounselingModel(Counseling counseling) {
        this.counselingid = counseling.getCounselingid();
        this.psyid = counseling.getPsyid();
        this.timestamp = counseling.getTimestamp();
        this.status = counseling.getStatus();
        this.type = counseling.getType();
    }
}
