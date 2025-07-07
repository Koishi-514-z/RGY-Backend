package org.example.rgybackend.DAO.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.rgybackend.DAO.CounselingDAO;
import org.example.rgybackend.Entity.Counseling;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Repository.CounselingRepository;
import org.example.rgybackend.Utils.NotExistException;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CounselingDAOImpl implements CounselingDAO {
    @Autowired
    private CounselingRepository counselingRepository;

    @Override
    public boolean counseled(String psyid, Long timestamp) {
        return counselingRepository.existsByPsyidAndTimestamp(psyid, timestamp);
    }

    @Override
    public Counseling getCounselingById(Long counselingid) {
        Optional<Counseling> counselingOptional = counselingRepository.findById(counselingid);
        if(counselingOptional.isEmpty()) {
            throw new NotExistException("Counseling not exists, counselingid: " + counselingid);
        }
        return counselingOptional.get();
    }

    @Override
    public List<CounselingModel> getCounseling(String psyid) {
        List<CounselingModel> counselingModels = new ArrayList<>();
        List<Counseling> counselings = counselingRepository.findByPsyid(psyid);
        for(Counseling counseling : counselings) {
            counselingModels.add(new CounselingModel(counseling));
        }
        return counselingModels;
    }

    @Override
    public List<CounselingModel> getUserCounseling(String userid) {
        List<CounselingModel> counselingModels = new ArrayList<>();
        List<Counseling> counselings = counselingRepository.findByUserid(userid);
        for(Counseling counseling : counselings) {
            counselingModels.add(new CounselingModel(counseling));
        }
        return counselingModels;
    }

    @Override
    public List<CounselingModel> getDateCounseling(String psyid, LocalDate date) {
        List<CounselingModel> counselingModels = new ArrayList<>();
        Long timestamp = TimeUtil.getStartOfDayTimestamp(date);
        List<Counseling> counselings = counselingRepository.scanCounseling(psyid, timestamp, timestamp + TimeUtil.DAY);
        for(Counseling counseling : counselings) {
            counselingModels.add(new CounselingModel(counseling));
        }
        return counselingModels;
    }

    @Override
    public boolean addCounseling(CounselingModel counselingModel, String userid) {
        counselingRepository.save(new Counseling(counselingModel, userid));
        return true;
    }

    @Override
    public boolean removeCounseling(Long counselingid) {
        counselingRepository.deleteById(counselingid);
        return true;
    }

    @Override
    public boolean setStatus(Long counselingid, Long status) {
        Optional<Counseling> counselingOptional = counselingRepository.findById(counselingid);
        if(counselingOptional.isEmpty()) {
            throw new NotExistException("Counseling not exists, counselingid: " + counselingid);
        }
        Counseling counseling = counselingOptional.get();
        counseling.setStatus(status);
        counselingRepository.save(counseling);
        return true;
    }

    @Override
    public boolean remove(Long counselingid) {
        counselingRepository.deleteById(counselingid);
        return true;
    }
}
