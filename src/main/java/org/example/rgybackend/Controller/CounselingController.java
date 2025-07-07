package org.example.rgybackend.Controller;

import java.util.List;

import org.example.rgybackend.DTO.PsyCommentData;
import org.example.rgybackend.Model.AvailableTimeModel;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Model.PsyProfileModel;
import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Service.CounselingService;
import org.example.rgybackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public List<CounselingModel> getCounseling(@RequestParam String psyid) {
        return counselingService.getCounseling(psyid);
    }

    @GetMapping("/getuser")
    public List<CounselingModel> getUserCounseling(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return counselingService.getUserCounseling(userid);
    }

    @GetMapping("/getdate")
    public List<CounselingModel> getDateCounseling(@RequestParam String psyid, @RequestParam Long timestamp) {
        return counselingService.getDateCounseling(psyid, timestamp);
    }

    @GetMapping("/getpsy")
    public PsyProfileModel getPsyProfile(@RequestParam String psyid) {
        return userService.getPsyProfile(psyid);
    }

    @GetMapping("/getpsys")
    public List<PsyProfileModel> getPsyProfiles() {
        return userService.getPsyProfiles();
    }

    @GetMapping("/gettags")
    public List<TagModel> getTypeTags() {
        return counselingService.getTypeTags();
    }

    @PostMapping("/add")
    public boolean addCounseling(@RequestBody CounselingModel counselingModel, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return counselingService.addCounseling(counselingModel, userid);
    }

    @PostMapping("/addcomm")
    public boolean addComment(@RequestBody PsyCommentData psyCommentData, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        psyCommentData.setUserid(userid);
        return counselingService.addComment(psyCommentData);
    }

    @PutMapping("/status")
    public boolean setStatus(@RequestParam Long counselingid, @RequestParam Long status, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        return counselingService.setStatus(counselingid, status, userid);
    }

    @DeleteMapping("/del")
    public boolean removeCounseling(@RequestParam Long counselingid) {
        return counselingService.removeCounseling(counselingid);
    }

    @GetMapping("/available/get")
    public AvailableTimeModel getAvailableTime(@RequestParam String psyid) {
        return counselingService.getAvailableTime(psyid);
    }

    @GetMapping("/available/getdate")
    public List<Long> getDateAvailables(@RequestParam String psyid, @RequestParam Long timestamp) {
        return counselingService.getDateAvailables(psyid, timestamp);
    }

    @PostMapping("/available/set")
    public boolean setAvailableTimes(@RequestBody AvailableTimeModel availableTimeModel, HttpSession session) {
        String psyid = (String)session.getAttribute("user");
        availableTimeModel.setPsyid(psyid);
        return counselingService.setAvailableTimes(availableTimeModel);
    }
}
