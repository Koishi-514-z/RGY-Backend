package org.example.rgybackend.Service;

import org.example.rgybackend.Model.CrisisModel;

import java.util.List;

public interface CrisisService {

    void saveCrisis(int crisisid);

    void deleteCrisis(int crisisid);

    void deleteCrisisAuditing(int crisisid);

    void saveCrisisAuditing(String content, Long timestamp, String userid);

    List<CrisisModel> getAllCrisis();

    List<CrisisModel> getAllCrisisAuditing();

    List<CrisisModel> getCrisisByUser(String userid);
}
