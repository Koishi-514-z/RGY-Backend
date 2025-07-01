package org.example.rgybackend.Controller;

import java.util.List;

import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Service.CounselingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/counseling")
public class CounselingController {
    @Autowired 
    private CounselingService counselingService;

    @GetMapping("/get")
    public List<CounselingModel> getCounseling(@RequestParam String psyid) {
        return counselingService.getCounseling(psyid);
    }

    @GetMapping("/get")
    public List<CounselingModel> getDateCounseling(@RequestParam String psyid, @RequestParam Long timestamp) {
        return counselingService.getDateCounseling(psyid, timestamp);
    }

    @PostMapping("/add")
    public boolean addCounseling(@RequestParam String psyid, @RequestParam Long timestamp, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return counselingService.addCounseling(userid, psyid, timestamp);
    }

    @DeleteMapping("/del")
    public boolean removeCounseling(@RequestParam Long counselingid) {
        return counselingService.removeCounseling(counselingid);
    }

    @GetMapping("/available/get")
    public List<Long> getAvailableTime(HttpSession session) {
        String psyid = (String)session.getAttribute("user");
        return counselingService.getAvailableTime(psyid);
    }

    @PostMapping("/available/set")
    public boolean setAvailableTimes(@RequestBody List<Long> timestamps, HttpSession session) {
        String psyid = (String)session.getAttribute("user");
        return counselingService.setAvailableTimes(psyid, timestamps);
    }

    @PostMapping("/available/add")
    public boolean addAvailableTime(@RequestBody Long timestamp, HttpSession session) {
        String psyid = (String)session.getAttribute("user");
        return counselingService.addAvailableTime(psyid, timestamp);
    }

    @DeleteMapping("/available/del")
    public boolean removeAvailableTime(@RequestBody Long timestamp, HttpSession session) {
        String psyid = (String)session.getAttribute("user");
        return counselingService.removeAvailableTime(psyid, timestamp);
    }
}
