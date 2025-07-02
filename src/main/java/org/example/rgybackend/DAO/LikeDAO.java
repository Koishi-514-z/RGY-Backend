package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.DTO.LikeData;

public interface LikeDAO {
    List<LikeData> findOppositeUser(String userid);
}
