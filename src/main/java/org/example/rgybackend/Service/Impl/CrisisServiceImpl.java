package org.example.rgybackend.Service.Impl;

import java.util.List;

import org.example.rgybackend.DAO.CrisisDAO;
import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Service.CrisisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrisisServiceImpl implements CrisisService {
    @Autowired
    private CrisisDAO crisisDAO;

    @Override
    public List<CrisisModel> getAllCrisis() {
        return crisisDAO.getAllCrisis();
    }

    // 0-->pending, 1-->processing, 2-->resolved
    @Override
    public boolean updateStatus(Long crisisid, Long status) {
        return crisisDAO.updateStatus(crisisid, status);
    }
}
