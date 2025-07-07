package org.example.rgybackend.DAO.Impl;

import java.util.Optional;

import org.example.rgybackend.DAO.PsyExtraDAO;
import org.example.rgybackend.DTO.PsyCommentData;
import org.example.rgybackend.Entity.PsyProfileExtra;
import org.example.rgybackend.Repository.PsyExtraRepository;
import org.example.rgybackend.Utils.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PsyExtraDAOImpl implements PsyExtraDAO {
    @Autowired
    private PsyExtraRepository psyExtraRepository;

    @Override
    public PsyProfileExtra getPsyProfileExtra(String psyid) {
        Optional<PsyProfileExtra> psyOptional = psyExtraRepository.findById(psyid);
        if(psyOptional.isEmpty()) {
            throw new NotExistException("Profile not exists, psyid: " + psyid);
        }
        return psyOptional.get();
    }

    @Override
    public boolean setPsyProfileExtra(PsyProfileExtra psyProfileExtra) {
        psyExtraRepository.save(psyProfileExtra);
        return true;
    }   

    @Override
    public boolean increaseClients(String psyid) {
        Optional<PsyProfileExtra> psyOptional = psyExtraRepository.findById(psyid);
        if(psyOptional.isEmpty()) {
            throw new NotExistException("Profile not exists, psyid: " + psyid);
        }
        PsyProfileExtra profileExtra = psyOptional.get();
        profileExtra.setTotalClients(profileExtra.getTotalClients() + 1);
        psyExtraRepository.save(profileExtra);
        return true;
    }
    
    @Override
    public boolean addComments(PsyCommentData psyCommentData) {
        String psyid = psyCommentData.getPsyid();
        Optional<PsyProfileExtra> psyOptional = psyExtraRepository.findById(psyid);
        if(psyOptional.isEmpty()) {
            throw new NotExistException("Profile not exists, psyid: " + psyid);
        }
        PsyProfileExtra profileExtra = psyOptional.get();
        profileExtra.setCommentNum(profileExtra.getCommentNum() + 1);
        profileExtra.setTotalScore(profileExtra.getTotalScore() + psyCommentData.getScore());
        if(psyCommentData.isSuccess()) {
            profileExtra.setSuccessNum(profileExtra.getSuccessNum() + 1);
        }
        psyExtraRepository.save(profileExtra);
        return true;
    }
}
