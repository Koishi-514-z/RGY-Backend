package org.example.rgybackend.DAO;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Entity.Counseling;
import org.example.rgybackend.Model.CounselingModel;

public interface CounselingDAO {
    boolean counseled(String psyid, Long timestamp);

    Counseling getCounselingById(Long counselingid);

    List<CounselingModel> getCounseling(String psyid);

    List<CounselingModel> getDateCounseling(String psyid, LocalDate date);
    
    boolean addCounseling(CounselingModel counselingModel, String userid);

    boolean setStatus(Long counselingid, Long status);

    boolean remove(Long counselingid);
}
