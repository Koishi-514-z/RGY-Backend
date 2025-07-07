package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.CrisisAuditingDAO;
import org.example.rgybackend.Entity.CrisisAuditing;
import org.example.rgybackend.Model.CrisisAuditingModel;
import org.example.rgybackend.Repository.CrisisAuditingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CrisisAuditingDAOImpl implements CrisisAuditingDAO {
    @Autowired
    private CrisisAuditingRepository crisisAuditingRepository;

    @Override
    public boolean addCrisis(CrisisAuditingModel crisisAuditingModel) {
        CrisisAuditing crisisAuditing = new CrisisAuditing(crisisAuditingModel);
        crisisAuditingRepository.save(crisisAuditing);
        return true;
    }
}
