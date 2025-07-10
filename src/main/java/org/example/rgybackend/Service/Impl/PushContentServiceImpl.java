package org.example.rgybackend.Service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DAO.PushContentDAO;
import org.example.rgybackend.DAO.QuoteDAO;
import org.example.rgybackend.DTO.PushContentSortDTO;
import org.example.rgybackend.DTO.SimplifiedUrlData;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.PushContentMatrix;
import org.example.rgybackend.Model.QuoteModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Service.EmotionService;
import org.example.rgybackend.Service.PushContentService;
import org.example.rgybackend.Utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PushContentServiceImpl implements PushContentService {
    @Autowired
    private PushContentDAO pushContentDAO;

    @Autowired
    private QuoteDAO quoteDAO;

    @Autowired
    private EmotionService emotionService;

    @Cacheable(value = "allPushContent")
    public List<UrlDataModel> getAllContent() {
        return pushContentDAO.getAllContent();
    }

    @Override
    @CacheEvict(value = "allPushContent", allEntries = true)
    public boolean pushContent(UrlDataModel urlDataModel) {
        urlDataModel.setCreatedAt(TimeUtil.now());
        return pushContentDAO.pushContent(urlDataModel);
    }

    @Override
    public List<SimplifiedUrlData> getSimplifiedContent() {
        return pushContentDAO.getSimplifiedContent();
    }

    @Override
    public List<UrlDataModel> getContentByTag(Long tagid, Integer pageIndex, Integer pageSize) {
        return pushContentDAO.getContentByTag(tagid, pageIndex, pageSize);
    }

    @Override
    public List<UrlDataModel> getContent(String userid, Integer pageIndex, Integer pageSize) {
        List<UrlDataModel> results = new ArrayList<>();
        List<UrlDataModel> urlDataModels = this.getAllContent();
        LocalDate today = TimeUtil.today();
        LocalDate prevThreeDay = today.minusDays(3);
        List<EmotionDataModel> emotionDataModels = emotionService.scanUserEmotionDatas(userid, prevThreeDay, today);

        double[] suitabilityArray = PushContentMatrix.getSuitabilityArray(emotionDataModels);
        List<PushContentSortDTO> pushContentSortDTOs = new ArrayList<>();

        for(UrlDataModel urlDataModel : urlDataModels) {
            double suitability = 0.0;
            for(TagModel tagModel : urlDataModel.getTags()) {
                suitability += suitabilityArray[tagModel.getId().intValue()];
            }
            suitability /= urlDataModel.getTags().size();

            int diffDays = (int)((TimeUtil.now() - urlDataModel.getCreatedAt()) / TimeUtil.DAY);
            double timeWeight = 1 / (diffDays + 1);

            pushContentSortDTOs.add(new PushContentSortDTO(urlDataModel, suitability * timeWeight));
        }

        pushContentSortDTOs.sort((s1, s2) -> Double.compare(s2.getRate(), s1.getRate()));

        for(int i = pageIndex * pageSize; i < Math.min((pageIndex + 1) * pageSize, pushContentSortDTOs.size()); ++i) {
            results.add(pushContentSortDTOs.get(i).getUrlDataModel());
        }

        return results;
    }

    @Override
    public Long getDataNum(Long tagid) {
        return pushContentDAO.getDataNum(tagid);
    }

    @Override
    public Long getAllDataNum() {
        return pushContentDAO.getAllDataNum();
    }

    @Override
    @CacheEvict(value = "allPushContent", allEntries = true)
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
