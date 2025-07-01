package org.example.rgybackend.DAO.Impl;

import java.util.List;

import org.example.rgybackend.DAO.LikeDAO;
import org.example.rgybackend.DTO.LikeData;
import org.example.rgybackend.Repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LikeDAOImpl implements LikeDAO {
    @Autowired
    private LikeRepository likeRepository;

    @Override
    public List<LikeData> findOppositeUser(String fromuserid) {
        return likeRepository.findOppositeUser(fromuserid);
    }
}
