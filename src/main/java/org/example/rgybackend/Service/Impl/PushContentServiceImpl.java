package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.PushContentDAO;
import org.example.rgybackend.Entity.PushContent;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Service.PushContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PushContentServiceImpl implements PushContentService {
    @Autowired
    private PushContentDAO pushContentDAO;

    @Override
    public void pushContent(UrlDataModel urlDataModel){
        pushContentDAO.pushContent(urlDataModel);
    }
    @Override
    public Page<PushContent> getContentByTag(Integer emotagid, Integer pageIndex, Integer pageSize){
        return pushContentDAO.getContentByTag(emotagid, pageIndex, pageSize);
    }
}
