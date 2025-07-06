package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.AvailableTimeModel;
import org.example.rgybackend.Model.CounselingModel;

public interface CounselingService {
    List<CounselingModel> getCounseling(String psyid);

    List<CounselingModel> getDateCounseling(String psyid, Long timestamp);

    boolean addCounseling(String userid, String psyid, Long timestamp);

    boolean setStatus(Long counselingid, Long status);

    boolean removeCounseling(Long counselingid);

    AvailableTimeModel getAvailableTime(String psyid);

    boolean setAvailableTimes(AvailableTimeModel availableTimeModel);
}
