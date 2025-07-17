package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Entity.CrisisAuditing;

public interface CrisisDAO {
    void saveCrisis(String content, Long timestamp, String userid, Long urgencyLevel,Long contentid);


    void deleteCrisisAuditing(int crisisid);


    List<Crisis> findAllCrisis();

    List<CrisisAuditing> findAllCrisisAuditing();

    CrisisAuditing findById(int crisisid);

    List<Crisis> findAllCrisisByUser(String userid);


    boolean updateStatus(Integer crisisid, Long status);
}
