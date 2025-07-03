package org.example.rgybackend.Service.Impl;

import java.util.List;

import org.example.rgybackend.DAO.PushContentDAO;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Service.PushContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushContentServiceImpl implements PushContentService {
    @Autowired
    private PushContentDAO pushContentDAO;

    @Override
    public boolean pushContent(UrlDataModel urlDataModel){
        return pushContentDAO.pushContent(urlDataModel);
    }

    @Override
    public List<UrlDataModel> getContentByTag(Integer emotagid, Integer pageIndex, Integer pageSize){
        return pushContentDAO.getContentByTag(emotagid, pageIndex, pageSize);
    }

    @Override
    public List<UrlDataModel> getContent(Integer pageIndex, Integer pageSize) {
        return pushContentDAO.getContent(pageIndex, pageSize);
    }

    @Override
    public Long getDataNum(Integer tagid) {
        return pushContentDAO.getDataNum(tagid);
    }

    @Override
    public Long getAllDataNum() {
        return pushContentDAO.getAllDataNum();
    }

    @Override
    public boolean deleteUrlData(Long dataid) {
        return pushContentDAO.deleteUrlData(dataid);
    }
}
