package org.example.rgybackend.DAO;

import org.example.rgybackend.Model.AvailableTimeModel;

public interface AvailableDAO {
    AvailableTimeModel getAvailableTime(String psyid);

    boolean setAvailableTimes(AvailableTimeModel availableTimeModel);
}
