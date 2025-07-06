package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.CrisisModel;

public interface CrisisService {
    List<CrisisModel> getAllCrisis();

    boolean updateStatus(Long crisisid, Long status);
}
