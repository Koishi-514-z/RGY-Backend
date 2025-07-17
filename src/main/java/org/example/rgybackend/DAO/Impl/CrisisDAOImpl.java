package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 

import org.example.rgybackend.DAO.CrisisDAO;
import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Entity.CrisisAuditing;
import org.example.rgybackend.Repository.CrisisAuditingRepository;
import org.example.rgybackend.Repository.CrisisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Utils.NotExistException;

@Repository
public class CrisisDAOImpl implements CrisisDAO {
    @Autowired
    private CrisisRepository crisisRepository;

    @Autowired
    private CrisisAuditingRepository crisisAuditingRepository;

    @Override
    public void saveCrisis(String content, Long timestamp, String userid,Long urgencyLevel,Long contentid) {
        Crisis crisis = new Crisis();
        crisis.setContent(content);
        crisis.setTimestamp(timestamp);
        crisis.setUserid(userid);
        crisis.setUrgencyLevel(urgencyLevel);
        crisis.setStatus(0L);
        crisis.setContentid(contentid);
        crisisRepository.save(crisis);
    }


    @Override
    public void deleteCrisisAuditing(int crisisid) {
        crisisAuditingRepository.deleteById(crisisid);
    }


    @Override
    public List<Crisis> findAllCrisis() {
        return crisisRepository.findAll();
    }

    @Override
    public List<CrisisAuditing> findAllCrisisAuditing() {
        return crisisAuditingRepository.findAll();
    }
    @Override
    public CrisisAuditing findById(int crisisid) {
        System.out.println(crisisid);

        return crisisAuditingRepository.findById(crisisid).orElse(null);
    }
    @Override
    public List<Crisis> findAllCrisisByUser(String userid) {
        return crisisRepository.findAllByUserid(userid);
    }


    @Override
    public boolean updateStatus(Integer crisisid, Long status) {
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
