package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.AvailableDAO;
import org.example.rgybackend.Entity.Available;
import org.example.rgybackend.Model.AvailableTimeModel;
import org.example.rgybackend.Repository.AvailableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AvailableDAOImpl implements AvailableDAO {
    @Autowired
    private AvailableRepository availableRepository;

    @Override
    public boolean isAvailable(String psyid, String date, Long hour) {
        return availableRepository.existsByPsyidAndDateAndHour(psyid, date, hour);
    }

    @Override
    public AvailableTimeModel getAvailableTime(String psyid) {
        AvailableTimeModel availableTimeModel = new AvailableTimeModel();
        availableTimeModel.setPsyid(psyid);
        availableTimeModel.setMonday(availableRepository.findAvailableTimes(psyid, "monday"));
        availableTimeModel.setTuesday(availableRepository.findAvailableTimes(psyid, "tuesday"));
        availableTimeModel.setWednesday(availableRepository.findAvailableTimes(psyid, "wednesday"));
        availableTimeModel.setThursday(availableRepository.findAvailableTimes(psyid, "thursday"));
        availableTimeModel.setFriday(availableRepository.findAvailableTimes(psyid, "friday"));
        availableTimeModel.setSaturday(availableRepository.findAvailableTimes(psyid, "saturday"));
        availableTimeModel.setSunday(availableRepository.findAvailableTimes(psyid, "sunday"));
        return availableTimeModel;
    }

    @Override
    public boolean setAvailableTimes(AvailableTimeModel availableTimeModel) {
        String psyid = availableTimeModel.getPsyid();
        availableRepository.deleteByPsyid(psyid);

        for(Long hour : availableTimeModel.getMonday()) {
            Available available = new Available(null, psyid, "monday", hour);
            availableRepository.save(available);
        }

        for(Long hour : availableTimeModel.getTuesday()) {
            Available available = new Available(null, psyid, "tuesday", hour);
            availableRepository.save(available);
        }

        for(Long hour : availableTimeModel.getWednesday()) {
            Available available = new Available(null, psyid, "wednesday", hour);
            availableRepository.save(available);
        }

        for(Long hour : availableTimeModel.getThursday()) {
            Available available = new Available(null, psyid, "thursday", hour);
            availableRepository.save(available);
        }

        for(Long hour : availableTimeModel.getFriday()) {
            Available available = new Available(null, psyid, "friday", hour);
            availableRepository.save(available);
        }

        for(Long hour : availableTimeModel.getSaturday()) {
            Available available = new Available(null, psyid, "saturday", hour);
            availableRepository.save(available);
        }

        for(Long hour : availableTimeModel.getSunday()) {
            Available available = new Available(null, psyid, "sunday", hour);
            availableRepository.save(available);
        }

        return true;
    }
}
