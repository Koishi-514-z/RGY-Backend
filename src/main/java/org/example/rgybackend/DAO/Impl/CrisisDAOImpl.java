package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.CrisisDAO;
import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Entity.CrisisAuditing;
import org.example.rgybackend.Repository.CrisisAuditingRepository;
import org.example.rgybackend.Repository.CrisisRepository;
import org.example.rgybackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CrisisDAOImpl implements CrisisDAO {
    @Autowired
    private CrisisRepository crisisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrisisAuditingRepository crisisAuditingRepository;

    @Override
    public void saveCrisis(String content, Long timestamp, String userid) {
        Crisis crisis = new Crisis();
        crisis.setContent(content);
        crisis.setTimestamp(timestamp);
        crisis.setUserid(userid);
        crisisRepository.save(crisis);
    }

    @Override
    public void deleteCrisis(int crisisid) {
        crisisRepository.deleteById(crisisid);
    }

    @Override
    public void deleteCrisisAuditing(int crisisid) {
        crisisAuditingRepository.deleteById(crisisid);
    }

    @Override
    public void saveCrisisAuditing(String content, Long timestamp, String userid) {
        CrisisAuditing crisisAuditing = new CrisisAuditing();
        crisisAuditing.setContent(content);
        crisisAuditing.setTimestamp(timestamp);
        crisisAuditing.setUserid(userid);
        crisisAuditingRepository.save(crisisAuditing);
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
        return crisisAuditingRepository.findById(crisisid).orElse(null);
    }
    @Override
    public List<Crisis> findAllCrisisByUser(String userid) {
        return crisisRepository.findAllByUserid(userid);
    }


}
