package org.example.rgybackend.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.ChatDAO;
import org.example.rgybackend.DTO.SessionTagDTO;
import org.example.rgybackend.Entity.Message;
import org.example.rgybackend.Entity.Session;
import org.example.rgybackend.Model.MessageModel;
import org.example.rgybackend.Model.SessionModel;
import org.example.rgybackend.Model.SessionTagModel;
import org.example.rgybackend.Service.ChatService;
import org.example.rgybackend.Service.MilestoneServive;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.SocketMessage;
import org.example.rgybackend.Utils.Notification;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDAO chatDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private Notification notification;

    @Autowired
    private MilestoneServive milestoneServive;

    @Override
    public SessionModel getSession(String fromuserid, Long sessionid) {
        Session session = chatDAO.getById(sessionid);
        List<MessageModel> messageModels = new ArrayList<>();
        SessionModel sessionModel = new SessionModel();

        sessionModel.setSessionid(sessionid);
        sessionModel.setTimestamp(session.getTimestamp());
        for(Message message : session.getMessages()) {
            if(fromuserid.equals(message.getFromuserid())) {
                messageModels.add(new MessageModel(message, 0L));
            }
            else if(fromuserid.equals(message.getTouserid())) {
                messageModels.add(new MessageModel(message, 1L));
            }
            else {
                throw new RuntimeException("Message does not contain such user, userid: " + fromuserid);
            }
        }
        messageModels.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));
        sessionModel.setMessages(messageModels);

        if(fromuserid.equals(session.getUserAid())) {
            sessionModel.setMyself(userService.getSimplifiedProfile(session.getUserAid()));
            sessionModel.setOther(userService.getSimplifiedProfile(session.getUserBid()));
            sessionModel.setUnread(session.getUnreadA());
        }
        else if(fromuserid.equals(session.getUserBid())) {
            sessionModel.setMyself(userService.getSimplifiedProfile(session.getUserBid()));
            sessionModel.setOther(userService.getSimplifiedProfile(session.getUserAid()));
            sessionModel.setUnread(session.getUnreadB());
        }
        else {
            throw new RuntimeException("Session does not contain such user, userid: " + fromuserid);
        }

        return sessionModel;
    }

    @Override
    public List<SessionTagModel> getSessionTags(String fromuserid) {
        List<SessionTagDTO> sessionTagDTOs = chatDAO.getSessionTags(fromuserid);
        List<SessionTagModel> sessionTagModels = new ArrayList<>();

        for(SessionTagDTO sessionTagDTO : sessionTagDTOs) {
            SessionTagModel sessionTagModel = new SessionTagModel();
            sessionTagModel.setSessionid(sessionTagDTO.getSessionid());
            sessionTagModel.setTimestamp(sessionTagDTO.getTimestamp());

            if(fromuserid.equals(sessionTagDTO.getUserAid())) {
                sessionTagModel.setMyself(userService.getSimplifiedProfile(sessionTagDTO.getUserAid()));
                sessionTagModel.setOther(userService.getSimplifiedProfile(sessionTagDTO.getUserBid()));
                sessionTagModel.setUnread(sessionTagDTO.getUnreadA());
            }
            else if(fromuserid.equals(sessionTagDTO.getUserBid())){
                sessionTagModel.setMyself(userService.getSimplifiedProfile(sessionTagDTO.getUserBid()));
                sessionTagModel.setOther(userService.getSimplifiedProfile(sessionTagDTO.getUserAid()));
                sessionTagModel.setUnread(sessionTagDTO.getUnreadB());
            }
            else {
                throw new RuntimeException("SessionTag does not contain such user, userid: " + fromuserid);
            }
            sessionTagModels.add(sessionTagModel);
        }

        sessionTagModels.sort((s1, s2) -> s2.getTimestamp().compareTo(s1.getTimestamp()));
        return sessionTagModels;
    }

    @Override
    public Long getSessionid(String fromuserid, String touserid) {
        return chatDAO.getSessionid(fromuserid, touserid);
    }

    @Override
    public boolean postMessage(Long sessionid, String content, String fromuserid) {
        Message message = new Message();
        Session session = chatDAO.getById(sessionid);
        String touserid = session.getUserAid().equals(fromuserid) ? session.getUserBid() : session.getUserAid();

        milestoneServive.addMilestone(fromuserid, 5L);

        message.setSessionid(sessionid);
        message.setFromuserid(fromuserid);
        message.setTouserid(touserid);
        message.setTimestamp(TimeUtil.now());
        message.setContent(content);

        boolean result = chatDAO.postMessage(sessionid, message);
        if(!result) {
            return false;
        }
        SocketMessage sockMessage = new SocketMessage("System", sessionid, fromuserid, touserid, TimeUtil.now(), content);
        notification.pushChatToUser(sockMessage);
        return true;
    }

    @Override
    public boolean updateRead(Long sessionid, String userid) {
        return chatDAO.updateRead(sessionid, userid);
    }

    @Override
    public Long createSession(String fromuserid, String touserid) {
        Session session = new Session();
        session.setUserAid(fromuserid);
        session.setUserBid(touserid);
        session.setTimestamp(TimeUtil.now());
        session.setUnreadA(0L);
        session.setUnreadB(0L);
        session.setMessages(new ArrayList<Message>());
        return chatDAO.createSession(session);
    }
    
}
