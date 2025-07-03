package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.DTO.ReplyData;

public interface ReplyDAO {
    List<ReplyData> findOppositeUser(String userid);
}
