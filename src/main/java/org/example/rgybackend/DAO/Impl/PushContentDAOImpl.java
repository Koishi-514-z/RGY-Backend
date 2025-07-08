package org.example.rgybackend.DAO.Impl;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.PushContentDAO;
import org.example.rgybackend.DTO.PushContentDTO;
import org.example.rgybackend.DTO.SimplifiedUrlData;
import org.example.rgybackend.Entity.PushContent;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Repository.PushContentRepository;
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
        PushContent pushContent = new PushContent(urlDataModel);
        pushContentRepository.save(pushContent);
        return true;
    }

    @Override
    public List<SimplifiedUrlData> getSimplifiedContent() {
        List<PushContentDTO> pushContents = pushContentRepository.findSimplifiedDatas();
        List<SimplifiedUrlData> simplifiedUrlDatas = new ArrayList<>();
        for(PushContentDTO pushContent : pushContents) {
            simplifiedUrlDatas.add(new SimplifiedUrlData(pushContent));
        }
        return simplifiedUrlDatas;
    }

    @Override
    public List<UrlDataModel> getContentByTag(Long tagid, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PushContent> pushContents = pushContentRepository.findAll(pageable);
        List<UrlDataModel> urlDataModels = new ArrayList<>();

        for(PushContent pushContent : pushContents) {
            UrlDataModel urlDataModel = new UrlDataModel(pushContent);
            boolean contain = false;
            for(TagModel tag : urlDataModel.getTags()) {
                if(tag.getId() == tagid) {
                    contain = true;
                }
            }
            if(contain) {
                urlDataModels.add(urlDataModel);
            }
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
    public Long getDataNum(Long tagid) {
        List<PushContent> pushContents = pushContentRepository.findAll();
        Long number = 0L;
        for(PushContent pushContent : pushContents) {
            UrlDataModel urlDataModel = new UrlDataModel(pushContent);
            for(TagModel tag : urlDataModel.getTags()) {
                if(tag.getId() == tagid) {
                    number++;
                    break;
                }
            }
        }
        return number;
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
