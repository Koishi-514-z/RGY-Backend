package org.example.rgybackend.Controller;

import java.util.List;

import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Service.CrisisService;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/getall")
    public List<CrisisModel> getAllCrisis(HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        return crisisService.getAllCrisis();
    }

    @PutMapping("update")
    public boolean updateCrisisStatus(@RequestParam Long crisisid, @RequestParam Long status, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userService.isAdmin(userid)) {
            throw new ForbiddenException("只有管理员允许进行该操作");
        }
        return crisisService.updateStatus(crisisid, status);
    }
}
