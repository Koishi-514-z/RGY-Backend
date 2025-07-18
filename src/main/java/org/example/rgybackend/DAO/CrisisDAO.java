package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Entity.CrisisAuditing;

public interface CrisisDAO {
    void saveCrisis(String content, Long timestamp, String userid, Long urgencyLevel,Long contentid);

    void deleteCrisis(int crisisid);

    void deleteCrisisAuditing(int crisisid);

    void saveCrisisAuditing(String content, Long timestamp, String userid);

    List<Crisis> findAllCrisis();

    List<CrisisAuditing> findAllCrisisAuditing();

    CrisisAuditing findById(int crisisid);

    List<Crisis> findAllCrisisByUser(String userid);

    List<CrisisModel> getAllCrisis();

    boolean addCrisis(CrisisModel crisisModel);

    boolean updateStatus(Integer crisisid, Long status);
}
