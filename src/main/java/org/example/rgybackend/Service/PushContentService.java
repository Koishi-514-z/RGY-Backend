package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.DTO.SimplifiedUrlData;
import org.example.rgybackend.Model.QuoteModel;
import org.example.rgybackend.Model.UrlDataModel;

public interface PushContentService {
    boolean pushContent(UrlDataModel urlDataModel);

    List<SimplifiedUrlData> getSimplifiedContent();

    List<UrlDataModel> getContentByTag(Long tagid, Integer pageIndex, Integer pageSize);

    List<UrlDataModel> getContent(String userid, Integer pageIndex, Integer pageSize);

    Long getDataNum(Long tagid);

    Long getAllDataNum();

    boolean deleteUrlData(Long dataid);

    QuoteModel getQuote();

    boolean addQuote(QuoteModel quoteModel);
}
