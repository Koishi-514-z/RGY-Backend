package org.example.rgybackend.DAO.Impl;

import java.util.List;

import org.example.rgybackend.DAO.MilestoneDAO;
import org.example.rgybackend.Entity.Milestone;
import org.example.rgybackend.Model.MilestoneModel;
import org.example.rgybackend.Repository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MilestoneDAOImpl implements MilestoneDAO {
    @Autowired
    private MilestoneRepository milestoneRepository;

    @Override
    public MilestoneModel getMilestone(String userid) {
        List<Milestone> milestones = milestoneRepository.findByUserid(userid);
        return new MilestoneModel(userid, milestones);
    }

    @Override
    public boolean addMilestone(Milestone milestone) {
        milestoneRepository.save(milestone);
        return true;
    }

    @Override
    public boolean milestoneExisted(String userid, Long milestoneid) {
        return milestoneRepository.existsByUseridAndMilestoneid(userid, milestoneid);
    }
}
