package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Model.CrisisModel;

public interface CrisisDAO {
    List<CrisisModel> getAllCrisis();

    boolean addCrisis(CrisisModel crisisModel);

    boolean updateStatus(Long crisisid, Long status);
}
