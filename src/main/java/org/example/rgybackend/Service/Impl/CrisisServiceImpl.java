package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.CrisisDAO;
import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Entity.CrisisAuditing;
import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Service.CrisisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrisisServiceImpl implements CrisisService {
    @Autowired
    private CrisisDAO crisisDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void saveCrisis(int crisisid,Long urgencyLevel) {
        CrisisAuditing crisisAuditing = crisisDAO.findById(crisisid);
        System.out.println(crisisAuditing);
        crisisDAO.saveCrisis(crisisAuditing.getContent(), crisisAuditing.getTimestamp(), crisisAuditing.getUserid(), urgencyLevel, crisisAuditing.getContentid());
        crisisDAO.deleteCrisisAuditing(crisisid);
    }

    public void deleteCrisis(int crisisid) {
        crisisDAO.deleteCrisis(crisisid);
    }
    @Override
    public void deleteCrisisAuditing(int crisisid) {
        crisisDAO.deleteCrisisAuditing(crisisid);
    }

    @Override
    public void saveCrisisAuditing(String content, Long timestamp, String userid) {
        crisisDAO.saveCrisisAuditing(content, timestamp, userid);
    }

    @Override
    public List<CrisisModel> getAllCrisis() {
        List<Crisis> crisisList = crisisDAO.findAllCrisis();
        List<CrisisModel> crisisModelList = new ArrayList<>();
        for(Crisis crisis : crisisList) {
            CrisisModel crisisModel = new CrisisModel();
            crisisModel.setCrisisid(crisis.getCrisisid());
            crisisModel.setContent(crisis.getContent());
            crisisModel.setTimestamp(crisis.getTimestamp());
            crisisModel.setUser(userDAO.getSimplified(crisis.getUserid()));
            crisisModel.setUserid(crisis.getUserid());
            crisisModel.setUrgencyLevel(crisis.getUrgencyLevel());
            crisisModel.setStatus(crisis.getStatus());
            crisisModelList.add(crisisModel);
        }
        return crisisModelList;
    }


    @Override
    public List<CrisisModel> getAllCrisisAuditing() {
        List<CrisisAuditing> crisisAuditingList = crisisDAO.findAllCrisisAuditing();
        List<CrisisModel> crisisModelList = new ArrayList<>();
        for(CrisisAuditing crisisAuditing : crisisAuditingList) {
            CrisisModel crisisModel = new CrisisModel();
            crisisModel.setCrisisid(crisisAuditing.getCrisisid());
            crisisModel.setContent(crisisAuditing.getContent());
            crisisModel.setTimestamp(crisisAuditing.getTimestamp());
            crisisModel.setUser(userDAO.getSimplified(crisisAuditing.getUserid()));
            crisisModel.setUserid(crisisAuditing.getUserid());
            crisisModelList.add(crisisModel);
        }
        return crisisModelList;
    }


    @Override
    public List<CrisisModel> getCrisisByUser(String userid) {
        List<Crisis> crisisList = crisisDAO.findAllCrisisByUser(userid);
        List<CrisisModel> crisisModelList = new ArrayList<>();
        for(Crisis crisis : crisisList) {
            CrisisModel crisisModel = new CrisisModel();
            crisisModel.setCrisisid(crisis.getCrisisid());
            crisisModel.setContent(crisis.getContent());
            crisisModel.setTimestamp(crisis.getTimestamp());
            crisisModel.setUser(userDAO.getSimplified(crisis.getUserid()));
            crisisModelList.add(crisisModel);
        }
        return crisisModelList;
    }
  
    // 0-->pending, 1-->processing, 2-->resolved
    @Override
    public boolean updateStatus(Integer crisisid, Long status) {
        return crisisDAO.updateStatus(crisisid, status);
    }
}
