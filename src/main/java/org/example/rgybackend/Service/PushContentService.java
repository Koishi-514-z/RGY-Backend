package org.example.rgybackend.Service;

import org.example.rgybackend.Entity.PushContent;
import org.example.rgybackend.Model.UrlDataModel;
import org.springframework.data.domain.Page;

public interface PushContentService {
    void pushContent(UrlDataModel urlDataModel);
    Page<PushContent> getContentByTag(Integer emotagid, Integer pageIndex, Integer pageSize);
}
