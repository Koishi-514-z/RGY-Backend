package org.example.rgybackend.Controller;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.DTO.StringDTO;
import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionModel;
import org.example.rgybackend.Model.EmotionDataModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;
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
    
    @GetMapping("/tag/get")
    public EmotionModel getEmotion() {
        return new EmotionModel(1L, "123456789", 6519874198741L, new TagModel(2L, "沮丧"), 2L);
    }

    @GetMapping("/tag/getall")
    public List<TagModel> getTags() {
        List<TagModel> datas = new ArrayList<>();
        datas.add(new TagModel(1L, "喜悦"));
        datas.add(new TagModel(2L, "沮丧"));
        datas.add(new TagModel(3L, "焦虑"));
        datas.add(new TagModel(4L, "迷茫"));
        datas.add(new TagModel(5L, "中性"));
        return datas;
    }

    @GetMapping("/url/get")
    public List<UrlDataModel> getUrlDatas() {
        List<UrlDataModel> datas = new ArrayList<>();
        datas.add(new UrlDataModel(1L, "music", "SSSSSS", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlDataModel(2L, "article", "DDDDDD", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlDataModel(3L, "music", "AAAAAA", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        return datas;
    }

    @GetMapping("/url/getall")
    public List<UrlDataModel> getAllUrlDatas() {
        List<UrlDataModel> datas = new ArrayList<>();
        datas.add(new UrlDataModel(1L, "music", "SSSSSS", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlDataModel(2L, "article", "DDDDDD", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlDataModel(3L, "music", "AAAAAA", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlDataModel(4L, "music", "SSSSSS", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlDataModel(5L, "article", "DDDDDD", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlDataModel(6L, "music", "AAAAAA", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        return datas;
    }

    @GetMapping("/negative")
    public boolean checkNegative() {
        return true;
    }

    @GetMapping("/diary/get")
    public DiaryModel getDiary(HttpSession session) {
        return new DiaryModel(1L, "123456789", 984650268L, 0L, "Hello World!");
    }

    @GetMapping("/data/getweek")
    public List<EmotionDataModel> getWeekData(HttpSession session) {
        List<EmotionDataModel> datas = new ArrayList<>();
        datas.add(new EmotionDataModel(1L, 1L));
        datas.add(new EmotionDataModel(2L, 2L));
        datas.add(new EmotionDataModel(3L, 3L));
        datas.add(new EmotionDataModel(4L, 4L));
        datas.add(new EmotionDataModel(5L, 5L));
        datas.add(new EmotionDataModel(6L, 3L));
        datas.add(new EmotionDataModel(7L, 2L));
        return datas;
    }

    @GetMapping("/data/getmonth")
    public List<EmotionDataModel> getMonthData(HttpSession session) {
        List<EmotionDataModel> datas = new ArrayList<>();
        datas.add(new EmotionDataModel(1L, 1L));
        datas.add(new EmotionDataModel(2L, 2L));
        datas.add(new EmotionDataModel(3L, 3L));
        datas.add(new EmotionDataModel(4L, 4L));
        datas.add(new EmotionDataModel(5L, 5L));
        datas.add(new EmotionDataModel(6L, 3L));
        datas.add(new EmotionDataModel(7L, 2L));
        datas.add(new EmotionDataModel(8L, 1L));
        datas.add(new EmotionDataModel(9L, 2L));
        datas.add(new EmotionDataModel(10L, 3L));
        datas.add(new EmotionDataModel(11L, 4L));
        datas.add(new EmotionDataModel(12L, 5L));
        datas.add(new EmotionDataModel(13L, 3L));
        datas.add(new EmotionDataModel(14L, 2L));
        datas.add(new EmotionDataModel(15L, 1L));
        datas.add(new EmotionDataModel(16L, 2L));
        datas.add(new EmotionDataModel(17L, 3L));
        datas.add(new EmotionDataModel(18L, 4L));
        datas.add(new EmotionDataModel(19L, 5L));
        datas.add(new EmotionDataModel(20L, 3L));
        datas.add(new EmotionDataModel(21L, 2L));
        datas.add(new EmotionDataModel(22L, 1L));
        datas.add(new EmotionDataModel(23L, 2L));
        datas.add(new EmotionDataModel(24L, 3L));
        datas.add(new EmotionDataModel(25L, 4L));
        datas.add(new EmotionDataModel(26L, 5L));
        datas.add(new EmotionDataModel(27L, 3L));
        datas.add(new EmotionDataModel(28L, 2L));
        datas.add(new EmotionDataModel(29L, 1L));
        datas.add(new EmotionDataModel(30L, 2L));
        return datas;
    }

    @GetMapping("/book")
    public boolean BookCounseling(@RequestParam Long timestamp) {
        return true;
    }

    @PutMapping("/tag/update")
    public boolean updateEmotion(@RequestBody EmotionModel emotion) {
        return true;
    }

    @PutMapping("/diary/update")
    public boolean updateDiary(@RequestBody StringDTO content) {
        return true;
    }
}


