package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.Model.SessionModel;
import org.example.rgybackend.Model.SessionTagModel;

public interface ChatService {
    SessionModel getSession(String fromuserid, Long sessionid);

    List<SessionTagModel> getSessionTags(String fromuserid);

    Long getSessionid(String fromuserid, String touserid);

    boolean postMessage(Long sessionid, String content, String fromuserid);

    boolean updateRead(Long sessionid, String userid);

    Long createSession(String fromuserid, String touserid);
}
