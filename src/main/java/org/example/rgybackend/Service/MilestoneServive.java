package org.example.rgybackend.Service;

import org.example.rgybackend.Model.MilestoneModel;

public interface MilestoneServive {
    MilestoneModel getMilestone(String userid);
    
    boolean addMilestone(String userid, Long milestoneid);
}
