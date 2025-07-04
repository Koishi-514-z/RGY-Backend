package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.PushContentDAO;
import org.example.rgybackend.Entity.PushContent;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Repository.PushContentRepository;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class PushContentDAOImpl implements PushContentDAO {
    @Autowired
    public PushContentRepository pushContentRepository;

    @Override
    public boolean pushContent(UrlDataModel urlDataModel) {
        PushContent pushContent = new PushContent(
                null,
                urlDataModel.getType(),
                urlDataModel.getTitle(),
                urlDataModel.getImg(),
                urlDataModel.getDescription(),
                urlDataModel.getUrl(),
                urlDataModel.getEmotagid(),
                TimeUtil.now()
        );
        pushContentRepository.save(pushContent);
        return true;
    }

    @Override
    public List<UrlDataModel> getContentByTag(Integer emotagid, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<PushContent> pushContents = pushContentRepository.findAllByEmotagidOrderByCreatedAtDesc(emotagid, pageable);
        List<UrlDataModel> urlDataModels = new ArrayList<>();
        for(PushContent pushContent : pushContents) {
            urlDataModels.add(new UrlDataModel(pushContent));
        }
        return urlDataModels;
    }

    @Override
    public List<UrlDataModel> getContent(Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PushContent> pushContents = pushContentRepository.findAll(pageable);
        List<UrlDataModel> urlDataModels = new ArrayList<>();
        for(PushContent pushContent : pushContents) {
            urlDataModels.add(new UrlDataModel(pushContent));
        }
        return urlDataModels;
    }

    @Override
    public Long getDataNum(Integer tagid) {
        return pushContentRepository.countByEmotagid(tagid);
    }

    @Override
    public Long getAllDataNum() {
        return pushContentRepository.count();
    }

    @Override
    public boolean deleteUrlData(Long dataid) {
        pushContentRepository.deleteById(dataid);
        return true;
    }
}
