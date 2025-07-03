package org.example.rgybackend.DAO.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.CounselingDAO;
import org.example.rgybackend.Entity.Counseling;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Repository.CounselingRepository;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CounselingDAOImpl implements CounselingDAO {
    @Autowired
    private CounselingRepository counselingRepository;

    @Override
    public boolean counseled(String psyid, Long timestamp) {
        return counselingRepository.existsByPsyidAndTimestamp(psyid, timestamp);
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
    public boolean addCounseling(CounselingModel counselingModel) {
        counselingRepository.save(new Counseling(counselingModel));
        return true;
    }

    @Override
    public boolean remove(Long counselingid) {
        counselingRepository.deleteById(counselingid);
        return true;
    }
}
