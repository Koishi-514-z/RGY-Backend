package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.CounselingModel;

public interface CounselingService {
    List<CounselingModel> getCounseling(String psyid);

    List<CounselingModel> getDateCounseling(String psyid, Long timestamp);

    boolean addCounseling(String userid, String psyid, Long timestamp);

    boolean removeCounseling(Long counselingid);

    List<Long> getAvailableTime(String psyid);

    boolean setAvailableTimes(String psyid, List<Long> timestamps);

    boolean addAvailableTime(String psyid, Long timestamp);

    boolean removeAvailableTime(String psyid, Long timestamp);
}
