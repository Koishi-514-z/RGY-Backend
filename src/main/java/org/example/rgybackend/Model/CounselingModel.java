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
    private String userid;
    private String psyid;
    private Long timestamp;

    public CounselingModel(Counseling counseling) {
        this.counselingid = counseling.getCounselingid();
        this.userid = counseling.getUserid();
        this.psyid = counseling.getPsyid();
        this.timestamp = counseling.getTimestamp();
    }
}
