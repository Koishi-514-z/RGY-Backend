package org.example.rgybackend.DAO.Impl;

import java.util.List;

import org.example.rgybackend.DAO.ReplyDAO;
import org.example.rgybackend.DTO.ReplyData;
import org.example.rgybackend.Repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDAOImpl implements ReplyDAO {
    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public List<ReplyData> findOppositeUser(String fromuserid) {
        return replyRepository.findOppositeUser(fromuserid);
    }
}
