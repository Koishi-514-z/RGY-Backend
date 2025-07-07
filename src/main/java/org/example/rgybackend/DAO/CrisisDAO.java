package org.example.rgybackend.DAO;

import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Entity.CrisisAuditing;

import java.util.List;

public interface CrisisDAO {
    void saveCrisis(String content, Long timestamp, String userid);

    void deleteCrisis(int crisisid);

    void deleteCrisisAuditing(int crisisid);

    void saveCrisisAuditing(String content, Long timestamp, String userid);

    List<Crisis> findAllCrisis();

    List<CrisisAuditing> findAllCrisisAuditing();

    CrisisAuditing findById(int crisisid);

    List<Crisis> findAllCrisisByUser(String userid);
}
