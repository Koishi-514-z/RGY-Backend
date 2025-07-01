package org.example.rgybackend.DAO.Impl;

import java.util.List;
import java.util.Optional;

import org.example.rgybackend.DAO.ChatDAO;
import org.example.rgybackend.DTO.SessionTagDTO;
import org.example.rgybackend.Entity.Message;
import org.example.rgybackend.Entity.Session;
import org.example.rgybackend.Repository.SessionRepository;
import org.example.rgybackend.Utils.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChatDAOImpl implements ChatDAO {
    @Autowired 
    private SessionRepository sessionRepository;

    @Override
    public Session getById(Long sessionid) {
        Optional<Session> sessOptional = sessionRepository.findById(sessionid);
        if(sessOptional.isEmpty()) {
            throw new NotExistException("Session not exists, sessionid: " + sessionid);
        }
        return sessOptional.get();
    }

    @Override
    public List<SessionTagDTO> getSessionTags(String fromuserid) {
        return sessionRepository.findSessionTags(fromuserid);
    }

    @Override
    public Long getSessionid(String fromuserid, String touserid) {
        List<Session> sessions = sessionRepository.findSession(fromuserid, touserid);
        if(sessions.size() == 0) {
            throw new NotExistException("Session not exists");
        }
        if(sessions.size() > 1) {
            throw new RuntimeException("Duplicate Session");
        }
        return sessions.get(0).getSessionid();
    }

    @Override
    public boolean postMessage(Long sessionid, Message message) {
        Optional<Session> sessOptional = sessionRepository.findById(sessionid);
        if(sessOptional.isEmpty()) {
            throw new NotExistException("Session not exists, sessionid: " + sessionid);
        }
        Session session = sessOptional.get();
        session.getMessages().add(message);
        sessionRepository.save(session);
        return true;
    }

    @Override
    public boolean updateRead(Long sessionid) {
        Optional<Session> sessOptional = sessionRepository.findById(sessionid);
        if(sessOptional.isEmpty()) {
            throw new NotExistException("Session not exists, sessionid: " + sessionid);
        }
        Session session = sessOptional.get();
        session.setUnread(0L);
        sessionRepository.save(session);
        return true;
    }

    @Override
    public Long createSession(Session session) {
        sessionRepository.save(session);
        List<Session> sessions = sessionRepository.findSession(session.getUserAid(), session.getUserBid());
        if(sessions.size() == 0) {
            throw new NotExistException("Session not exists");
        }
        if(sessions.size() > 1) {
            throw new RuntimeException("Duplicate Session");
        }
        return sessions.get(0).getSessionid();
    }
}
