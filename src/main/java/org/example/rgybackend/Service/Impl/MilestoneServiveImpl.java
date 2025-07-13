package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.MilestoneDAO;
import org.example.rgybackend.Entity.Milestone;
import org.example.rgybackend.Model.MilestoneModel;
import org.example.rgybackend.Service.MilestoneServive;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MilestoneServiveImpl implements MilestoneServive {
    @Autowired
    private MilestoneDAO milestoneDAO;

    @Override
    public MilestoneModel getMilestone(String userid) {
        return milestoneDAO.getMilestone(userid);
    }

    @Override
    public boolean addMilestone(String userid, Long milestoneid) {
        if(milestoneDAO.milestoneExisted(userid, milestoneid)) {
            return false;
        }
        return milestoneDAO.addMilestone(new Milestone(null, userid, milestoneid, TimeUtil.now()));
    }
}
