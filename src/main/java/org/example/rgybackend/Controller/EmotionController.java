package org.example.rgybackend.Controller;

import java.util.List;

import org.example.rgybackend.DTO.EmotionData;
import org.example.rgybackend.DTO.EmotionRecord;
import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Service.EmotionService;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/emotion")
public class EmotionController {
    @Autowired
    private EmotionService emotionService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/tag/get")
    public EmotionModel getEmotion(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.getEmotion(userid);
    }

    @GetMapping("/record/getnum")
    public Long getRecordNum(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.getRecordNum(userid);
    }

    @GetMapping("/record/get")
    public List<EmotionRecord> getHistoryRecords(@RequestParam Long pageIndex, @RequestParam Long pageSize, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.getHistoryRecords(pageIndex, pageSize, userid);
    }

    @GetMapping("/tag/getall")
    public List<TagModel> getTags() {
        return emotionService.getTags();
    }

    @GetMapping("/tag/geturl")
    public List<TagModel> getUrlTags() {
        return emotionService.getUrlTags();
    }

    @GetMapping("/negative")
    public boolean checkNegative(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.checkNegative(userid);
    }

    @GetMapping("/diary/get")
    public DiaryModel getDiary(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.getDiary(userid);
    }

    @GetMapping("/data/getweek")
    public List<EmotionDataModel> getWeekData(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.getWeekData(userid);
    }

    @GetMapping("/data/getmonth")
    public List<EmotionDataModel> getMonthData(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.getMonthData(userid);
    }

    @GetMapping("/data/scan")
    public EmotionData scanEmotionData(@RequestParam Long start, @RequestParam Long end, @RequestParam Long interval, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        return emotionService.scanEmotionData(start, end, interval);
    }

    @PutMapping("/tag/update")
    public boolean setEmotion(@RequestBody EmotionModel emotion, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        emotion.setUserid(userid);
        return emotionService.setEmotion(emotion);
    }

    @PutMapping("/diary/update")
    public boolean updateDiary(@RequestBody StringDTO content, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.updateDiary(userid, content.getStr());
    }
}


