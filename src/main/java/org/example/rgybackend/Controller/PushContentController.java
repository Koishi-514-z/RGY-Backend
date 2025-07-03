package org.example.rgybackend.Controller;

import org.example.rgybackend.Entity.PushContent;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Service.PushContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pushcontent")
public class PushContentController {
    @Autowired
    PushContentService pushContentService;

    @PostMapping("/push")
    public void pushContent(@RequestBody UrlDataModel urlDataModel) {
        pushContentService.pushContent(urlDataModel);
    }

    @GetMapping("/getbytag")
    public Page<PushContent> getContentByTag(@RequestParam Integer emotagid, @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return pushContentService.getContentByTag(emotagid, pageIndex, pageSize);
    }
}
