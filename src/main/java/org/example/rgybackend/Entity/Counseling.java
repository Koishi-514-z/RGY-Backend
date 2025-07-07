package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.CounselingModel;

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
@Table(name = "counseling")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Counseling {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "counselingid")
    private Long counselingid;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "psyid")
    private String psyid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "status")
    private Long status;

    @Basic
    @Column(name = "type")
    private Long type;

    public Counseling(CounselingModel counselingModel, String userid) {
        this.counselingid = counselingModel.getCounselingid();
        this.userid = userid;
        this.psyid = counselingModel.getPsyid();
        this.timestamp = counselingModel.getTimestamp();
        this.status = counselingModel.getStatus();
        this.type = counselingModel.getType().getId();
    }
}
