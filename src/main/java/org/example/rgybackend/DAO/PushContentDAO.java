package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.DTO.SimplifiedUrlData;
import org.example.rgybackend.Model.UrlDataModel;

public interface PushContentDAO {
    boolean pushContent(UrlDataModel urlDataModel);

    List<SimplifiedUrlData> getSimplifiedContent();

    List<UrlDataModel> getAllContent();

    List<UrlDataModel> getContentByTag(Long tagid, Integer pageIndex, Integer pageSize);

    List<UrlDataModel> getContent(Integer pageIndex, Integer pageSize);

    Long getDataNum(Long tagid);

    Long getAllDataNum();

    boolean deleteUrlData(Long dataid);
}
