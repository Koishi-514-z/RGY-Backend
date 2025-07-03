package org.example.rgybackend.DAO;

import org.example.rgybackend.Entity.PushContent;
import org.example.rgybackend.Model.UrlDataModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PushContentDAO {
    void pushContent(UrlDataModel urlDataModel);
    Page<PushContent> getContentByTag(Integer emotagid, Integer pageIndex, Integer pageSize);
}
