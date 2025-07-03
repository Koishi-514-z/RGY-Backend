package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Model.UrlDataModel;

public interface PushContentDAO {
    boolean pushContent(UrlDataModel urlDataModel);

    List<UrlDataModel> getContentByTag(Integer emotagid, Integer pageIndex, Integer pageSize);

    List<UrlDataModel> getContent(Integer pageIndex, Integer pageSize);

    Long getDataNum(Integer tagid);

    Long getAllDataNum();

    boolean deleteUrlData(Long dataid);
}
