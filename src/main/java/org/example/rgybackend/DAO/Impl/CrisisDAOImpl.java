package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.rgybackend.DAO.CrisisDAO;
import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Repository.CrisisRepository;
import org.example.rgybackend.Utils.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CrisisDAOImpl implements CrisisDAO {
    @Autowired
    private CrisisRepository crisisRepository;

    @Override
    public List<CrisisModel> getAllCrisis() {
        List<Crisis> crisises = crisisRepository.findAll();
        List<CrisisModel> crisisModels = new ArrayList<>();
        for(Crisis crisis : crisises) {
            crisisModels.add(new CrisisModel(crisis));
        }
        return crisisModels;
    }

    @Override
    public boolean addCrisis(CrisisModel crisisModel) {
        Crisis crisis = new Crisis(crisisModel);
        crisisRepository.save(crisis);
        return true;
    }

    @Override
    public boolean updateStatus(Long crisisid, Long status) {
        Optional<Crisis> crisisOptional = crisisRepository.findById(crisisid);
        if(crisisOptional.isEmpty()) {
            throw new NotExistException("Crisis not exists, crisisid: " + crisisid);
        }
        Crisis crisis = crisisOptional.get();
        crisis.setStatus(status);
        crisisRepository.save(crisis);
        return true;
    }
}
