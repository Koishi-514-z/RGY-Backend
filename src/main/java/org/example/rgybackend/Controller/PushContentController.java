package org.example.rgybackend.Controller;

import java.util.List;

import org.example.rgybackend.Model.QuoteModel;
import org.example.rgybackend.Model.UrlDataModel;
import org.example.rgybackend.Service.PushContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pushcontent")
public class PushContentController {
    @Autowired
    PushContentService pushContentService;

    @GetMapping("/getbytag")
    public List<UrlDataModel> getContentByTag(@RequestParam Integer tagid, @RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return pushContentService.getContentByTag(tagid, pageIndex, pageSize);
    }

    @GetMapping("/get")
    public List<UrlDataModel> getContent(@RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return pushContentService.getContent(pageIndex, pageSize);
    }

    @GetMapping("/getnum")
    public Long getDataNum(@RequestParam Integer tagid) {
        return pushContentService.getDataNum(tagid);
    }

    @GetMapping("/getallnum")
    public Long getAllDataNum() {
        return pushContentService.getAllDataNum();
    }

    @GetMapping("/quote/get")
    public QuoteModel getQuote() {
        return pushContentService.getQuote();
    }

    @PostMapping("/push")
    public boolean pushContent(@RequestBody UrlDataModel urlDataModel) {
        return pushContentService.pushContent(urlDataModel);
    }

    @PostMapping("/quote/add")
    public boolean AddQuote(@RequestBody QuoteModel quoteModel) {
        return pushContentService.addQuote(quoteModel);
    }

    @DeleteMapping("/del")
    public boolean deleteUrlData(@RequestParam Long urlid) {
        return pushContentService.deleteUrlData(urlid);
    }
}
