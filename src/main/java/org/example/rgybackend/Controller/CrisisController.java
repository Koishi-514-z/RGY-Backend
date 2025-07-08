package org.example.rgybackend.Controller;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Service.CrisisService;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/crisis")
public class CrisisController {
    @Autowired
    private CrisisService crisisService;
  
    @Autowired
    private UserService userService;

    @GetMapping("/listAuditing")
    public List<CrisisModel> listCrisisAuditing(HttpSession session) {

        return crisisService.getAllCrisisAuditing();
    }

    @GetMapping("/listUser/{userid}")
    public List<CrisisModel> listCrisisByUser(@PathVariable String userid, HttpSession session) {
        String id = (String)session.getAttribute("user");
        if(!userService.isAdmin(id)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        return crisisService.getCrisisByUser(userid);
    }

    @PostMapping("/confirm")
    public boolean confirmCrisis(@RequestBody String json,HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        JSONObject obj = JSONObject.parseObject(json);
        int crisisid = obj.getIntValue("crisisid");
        Long urgencyLevel = obj.getLongValue("urgencyLevel");
        crisisService.saveCrisis(crisisid, urgencyLevel);
        return true;
    }

    @PostMapping("/delete")
    public boolean deleteCrisisAuditing(@RequestBody String json,HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        JSONObject obj = JSONObject.parseObject(json);
        int crisisid = obj.getIntValue("crisisid");
        crisisService.deleteCrisisAuditing(crisisid);
        return true;
    }

    @GetMapping("/getall")
    public List<CrisisModel> getAllCrisis(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        return crisisService.getAllCrisis();
    }

    @PutMapping("update")
    public boolean updateCrisisStatus(@RequestParam Integer crisisid, @RequestParam Long status, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        return crisisService.updateStatus(crisisid, status);
    }
}
