package org.example.rgybackend.DAO;

import org.example.rgybackend.Entity.Milestone;
import org.example.rgybackend.Model.MilestoneModel;

public interface MilestoneDAO {
    MilestoneModel getMilestone(String userid);

    boolean addMilestone(Milestone milestone);

    boolean milestoneExisted(String userid, Long milestoneid);
}
