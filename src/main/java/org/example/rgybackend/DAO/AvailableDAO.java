package org.example.rgybackend.DAO;

import org.example.rgybackend.Model.AvailableTimeModel;

public interface AvailableDAO {
    boolean isAvailable(String psyid, String date, Long hour);

    AvailableTimeModel getAvailableTime(String psyid);

    boolean setAvailableTimes(AvailableTimeModel availableTimeModel);
}
