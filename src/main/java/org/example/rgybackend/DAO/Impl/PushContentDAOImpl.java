package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.PushContentDAO;
import org.example.rgybackend.Entity.PushContent;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Repository.PushContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class PushContentDAOImpl implements PushContentDAO {
    @Autowired
    public PushContentRepository pushContentRepository;

    @Override
    public void pushContent(UrlDataModel urlDataModel) {
        PushContent pushContent = new PushContent(
                null,
                urlDataModel.getType(),
                urlDataModel.getTitle(),
                urlDataModel.getImg(),
                urlDataModel.getDescription(),
                urlDataModel.getUrl(),
                urlDataModel.getEmotagid(),
                new Timestamp(System.currentTimeMillis())
        );
        pushContentRepository.save(pushContent);
    }
    @Override
    public Page<PushContent> getContentByTag(Integer emotagid, Integer pageIndex, Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return pushContentRepository.findAllByEmotagidOrderByCreatedAtDesc(emotagid,pageable);
    }
}
