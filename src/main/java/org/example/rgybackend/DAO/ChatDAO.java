package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.DTO.SessionTagDTO;
import org.example.rgybackend.Entity.Message;
import org.example.rgybackend.Entity.Session;

public interface ChatDAO {
    Session getById(Long sessionid);

    List<SessionTagDTO> getSessionTags(String fromuserid);

    Long getSessionid(String fromuserid, String touserid);

    boolean postMessage(Long sessionid, Message message);

    boolean updateRead(Long sessionid);

    Long createSession(Session session);
}
