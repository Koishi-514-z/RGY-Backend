package org.example.rgybackend.Service;

import org.example.rgybackend.Model.CrisisModel;

import java.util.List;

public interface CrisisService {
    void saveCrisis(int crisisid,Long urgencyLevel);


    void deleteCrisisAuditing(int crisisid);



    List<CrisisModel> getAllCrisis();

    List<CrisisModel> getAllCrisisAuditing();

    List<CrisisModel> getCrisisByUser(String userid);
  
    boolean updateStatus(Integer crisisid, Long status);
}
