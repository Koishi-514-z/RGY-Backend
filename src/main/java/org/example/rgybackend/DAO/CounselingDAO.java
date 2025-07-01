package org.example.rgybackend.DAO;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Model.CounselingModel;

public interface CounselingDAO {
    boolean counseled(String psyid, Long timestamp);

    List<CounselingModel> getCounseling(String psyid);

    List<CounselingModel> getDateCounseling(String psyid, LocalDate date);
    
    boolean addCounseling(CounselingModel counselingModel);

    boolean remove(Long counselingid);
}
