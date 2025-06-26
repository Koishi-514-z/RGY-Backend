package org.example.rgybackend.Controller;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Model.Diary;
import org.example.rgybackend.Model.Emotion;
import org.example.rgybackend.Model.EmotionData;
import org.example.rgybackend.Model.Tag;
import org.example.rgybackend.Model.UrlData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/emotion")
public class EmotionController {
    
    @GetMapping("/tag/get")
    public Emotion getEmotion() {
        return new Emotion(1L, "123456789", 6519874198741L, new Tag(2L, "沮丧"), 2L);
    }

    @GetMapping("/tag/getall")
    public List<Tag> getTags() {
        List<Tag> datas = new ArrayList<>();
        datas.add(new Tag(1L, "喜悦"));
        datas.add(new Tag(2L, "沮丧"));
        datas.add(new Tag(3L, "焦虑"));
        datas.add(new Tag(4L, "迷茫"));
        datas.add(new Tag(5L, "中性"));
        return datas;
    }

    @GetMapping("/url/get")
    public List<UrlData> getUrlDatas() {
        List<UrlData> datas = new ArrayList<>();
        datas.add(new UrlData(1L, "music", "SSSSSS", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlData(2L, "article", "DDDDDD", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        datas.add(new UrlData(3L, "music", "AAAAAA", null, "oqeuirouhyb", "https://github.com/Koishi-514-z/RGY-Frontend"));
        return datas;
    }

    @GetMapping("/negative")
    public boolean checkNegative() {
        return true;
    }

    @GetMapping("/diary/get")
    public Diary getDiary(HttpSession session) {
        return new Diary(1L, "123456789", 984650268L, 0L, "Hello World!");
    }

    @GetMapping("/data/getweek")
    public List<EmotionData> getWeekData(HttpSession session) {
        List<EmotionData> datas = new ArrayList<>();
        datas.add(new EmotionData(1L, 1L));
        datas.add(new EmotionData(2L, 2L));
        datas.add(new EmotionData(3L, 3L));
        datas.add(new EmotionData(4L, 4L));
        datas.add(new EmotionData(5L, 5L));
        datas.add(new EmotionData(6L, 3L));
        datas.add(new EmotionData(7L, 2L));
        return datas;
    }

    @GetMapping("/data/getmonth")
    public List<EmotionData> getMonthData(HttpSession session) {
        List<EmotionData> datas = new ArrayList<>();
        datas.add(new EmotionData(1L, 1L));
        datas.add(new EmotionData(2L, 2L));
        datas.add(new EmotionData(3L, 3L));
        datas.add(new EmotionData(4L, 4L));
        datas.add(new EmotionData(5L, 5L));
        datas.add(new EmotionData(6L, 3L));
        datas.add(new EmotionData(7L, 2L));
        return datas;
    }

    @PutMapping("/tag/update")
    public boolean updateEmotion(@RequestBody Emotion emotion) {
        return true;
    }

    @PutMapping("/diary/update")
    public boolean updateDiary(@RequestBody Diary diary) {
        return true;
    }
}


