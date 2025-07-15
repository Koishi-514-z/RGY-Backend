package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.BlogDAO;
import org.example.rgybackend.DAO.CrisisDAO;
import org.example.rgybackend.DAO.NotificationPrivateDAO;
import org.example.rgybackend.Entity.Crisis;
import org.example.rgybackend.Entity.CrisisAuditing;
import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Model.NotificationPrivateModel;
import org.example.rgybackend.Service.CrisisService;
import org.example.rgybackend.Service.SjtuMailService;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.NotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrisisServiceImpl implements CrisisService {
    @Autowired
    private CrisisDAO crisisDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogDAO blogDAO;

    @Autowired
    private NotificationPrivateDAO notificationPrivateDAO;

    @Autowired
    private SjtuMailService sjtuMailService;

    @Override
    public void saveCrisis(int crisisid, Long urgencyLevel) {
        CrisisAuditing crisisAuditing = crisisDAO.findById(crisisid);

        if(crisisAuditing != null){
            if(crisisAuditing.getType() == 0){
                blogDAO.deleteBlog(crisisAuditing.getContentid());
            }
            else if(crisisAuditing.getType() == 1){
                blogDAO.deleteReply(crisisAuditing.getContentid());
            }
        }

        NotificationPrivateModel notification = new NotificationPrivateModel(NotificationUtil.crisis);
        notification.setAdminid("System");
        notification.setUserid(crisisAuditing.getUserid());
        notificationPrivateDAO.addNotification(notification);

        if(urgencyLevel >= 2) {
            String email = userService.getUserProfile(crisisAuditing.getUserid()).getEmail();
            String subject = "【温馨提醒】我们关心您的安全与健康";
            String content =
                "您好，\n\n"
                + "我们注意到您最近在社区发布的内容中，系统检测到存在自伤或自杀等高危倾向。\n"
                + "如果您正经历情绪低落或困扰，建议您及时与身边的亲友沟通，或寻求专业心理援助。\n\n"
                + "如有紧急情况，请立即拨打心理援助热线（如：12320、400-161-9995）或报警电话寻求帮助。\n\n"
                + "您并不孤单，我们和社区都非常关心您的安全与健康。\n\n"
                + "—— 心理健康互助社区团队";
            sjtuMailService.sendSimpleMail(email, subject, content);
        }

        crisisDAO.saveCrisis(crisisAuditing.getContent(), crisisAuditing.getTimestamp(), crisisAuditing.getUserid(), urgencyLevel, crisisAuditing.getContentid());
        crisisDAO.deleteCrisisAuditing(crisisid);
    }

    public void deleteCrisis(int crisisid) {
        crisisDAO.deleteCrisis(crisisid);
    }
    @Override
    public void deleteCrisisAuditing(int crisisid) {
        CrisisAuditing crisisAuditing = crisisDAO.findById(crisisid);
        crisisDAO.deleteCrisisAuditing(crisisid);
    }

    @Override
    public void saveCrisisAuditing(String content, Long timestamp, String userid) {
        crisisDAO.saveCrisisAuditing(content, timestamp, userid);
    }

    @Override
    public List<CrisisModel> getAllCrisis() {
        List<Crisis> crisisList = crisisDAO.findAllCrisis();
        List<CrisisModel> crisisModelList = new ArrayList<>();
        for(Crisis crisis : crisisList) {
            CrisisModel crisisModel = new CrisisModel();
            crisisModel.setCrisisid(crisis.getCrisisid());
            crisisModel.setContent(crisis.getContent());
            crisisModel.setTimestamp(crisis.getTimestamp());
            crisisModel.setUser(userService.getSimplifiedProfile(crisis.getUserid()));
            crisisModel.setUserid(crisis.getUserid());
            crisisModel.setUrgencyLevel(crisis.getUrgencyLevel());
            crisisModel.setStatus(crisis.getStatus());
            crisisModelList.add(crisisModel);
        }
        return crisisModelList;
    }


    @Override
    public List<CrisisModel> getAllCrisisAuditing() {
        List<CrisisAuditing> crisisAuditingList = crisisDAO.findAllCrisisAuditing();
        List<CrisisModel> crisisModelList = new ArrayList<>();
        for(CrisisAuditing crisisAuditing : crisisAuditingList) {
            CrisisModel crisisModel = new CrisisModel();
            crisisModel.setCrisisid(crisisAuditing.getCrisisid());
            crisisModel.setContent(crisisAuditing.getContent());
            crisisModel.setTimestamp(crisisAuditing.getTimestamp());
            crisisModel.setUser(userService.getSimplifiedProfile(crisisAuditing.getUserid()));
            crisisModel.setUserid(crisisAuditing.getUserid());
            crisisModel.setUrgencyLevel(crisisAuditing.getUrgencyLevel());
            crisisModelList.add(crisisModel);
        }
        return crisisModelList;
    }


    @Override
    public List<CrisisModel> getCrisisByUser(String userid) {
        List<Crisis> crisisList = crisisDAO.findAllCrisisByUser(userid);
        List<CrisisModel> crisisModelList = new ArrayList<>();
        for(Crisis crisis : crisisList) {
            CrisisModel crisisModel = new CrisisModel();
            crisisModel.setCrisisid(crisis.getCrisisid());
            crisisModel.setContent(crisis.getContent());
            crisisModel.setTimestamp(crisis.getTimestamp());
            crisisModel.setUser(userService.getSimplifiedProfile(crisis.getUserid()));
            crisisModelList.add(crisisModel);
        }
        return crisisModelList;
    }
  
    // 0-->pending, 1-->processing, 2-->resolved
    @Override
    public boolean updateStatus(Integer crisisid, Long status) {
        return crisisDAO.updateStatus(crisisid, status);
    }
}
