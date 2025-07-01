package org.example.rgybackend.DAO.Impl;

import java.util.List;

import org.example.rgybackend.DAO.AvailableDAO;
import org.example.rgybackend.Entity.Available;
import org.example.rgybackend.Repository.AvailableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AvailableDAOImpl implements AvailableDAO {
    @Autowired
    private AvailableRepository availableRepository;

    @Override
    public boolean isAvailable(String psyid, Long timestamp) {
        return availableRepository.existsByPsyidAndTimestamp(psyid, timestamp);
    }

    @Override
    public List<Long> getAvailableTime(String psyid) {
        return availableRepository.findAvailableTimes(psyid);
    }

    @Override
    public boolean setAvailableTimes(String psyid, List<Long> timestamps) {
        availableRepository.deleteByPsyid(psyid);
        for(Long timestamp : timestamps) {
            Available available = new Available();
            available.setPsyid(psyid);
            available.setTimestamp(timestamp);
            availableRepository.save(available);
        }
        return true;
    }

    @Override
    public boolean addAvailableTime(String psyid, Long timestamp) {
        boolean existed = availableRepository.existsByPsyidAndTimestamp(psyid, timestamp);
        if(existed) {
            return false;
        }
        Available available = new Available();
        available.setPsyid(psyid);
        available.setTimestamp(timestamp);
        availableRepository.save(available);
        return true;
    }

    @Override
    public boolean removeAvailableTime(String psyid, Long timestamp) {
        return availableRepository.deleteByPsyidAndTimestamp(psyid, timestamp);
    }

    @Override
    public boolean removeAll(String psyid) {
        return availableRepository.deleteByPsyid(psyid);
    }
}
