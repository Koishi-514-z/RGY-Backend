package org.example.rgybackend.DAO;

import java.util.List;

public interface AvailableDAO {
    boolean isAvailable(String psyid, Long timestamp);

    List<Long> getAvailableTime(String psyid);

    boolean setAvailableTimes(String psyid, List<Long> timestamps);

    boolean addAvailableTime(String psyid, Long timestamp);

    boolean removeAvailableTime(String psyid, Long timestamp);

    boolean removeAll(String psyid);
}
