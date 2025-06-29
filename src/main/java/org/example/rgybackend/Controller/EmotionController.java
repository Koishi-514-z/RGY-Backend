package org.example.rgybackend.Controller;

import java.util.List;

import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;
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

    @GetMapping("/tag/getall")
    public List<TagModel> getTags() {
        return emotionService.getTags();
    }

    @GetMapping("/url/get")
    public List<UrlDataModel> getUrlDatas(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return emotionService.getUrlDatas(userid);
    }

    @GetMapping("/url/getall")
    public List<UrlDataModel> getAllUrlDatas(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("Only Administration can do this operation");
        }
        return emotionService.getAllUrlDatas();
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

    @GetMapping("/book")
    public boolean BookCounseling(@RequestParam Long timestamp) {
        return emotionService.BookCounseling(timestamp);
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


