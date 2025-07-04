package org.example.rgybackend.Service.Impl;

import java.util.List;

import org.example.rgybackend.DAO.PushContentDAO;
import org.example.rgybackend.DAO.QuoteDAO;
import org.example.rgybackend.Model.QuoteModel;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Service.PushContentService;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushContentServiceImpl implements PushContentService {
    @Autowired
    private PushContentDAO pushContentDAO;

    @Autowired
    private QuoteDAO quoteDAO;

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

    @Override
    public QuoteModel getQuote() {
        return quoteDAO.getQuote();
    }

    @Override
    public boolean addQuote(QuoteModel quoteModel) {
        return quoteDAO.addQuote(quoteModel, TimeUtil.now());
    }
}
