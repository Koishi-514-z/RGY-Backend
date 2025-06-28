package org.example.rgybackend.Controller;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;
import org.example.rgybackend.Model.UserModel;
import org.example.rgybackend.Service.UserService;
import org.example.rgybackend.Utils.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public boolean login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // boolean result = userService.verifyPasswordByName(username, password);
        // if(result) {
        //     String userid = userService.getProfileByName(username).getUserid();
        //     session.setAttribute("user", userid);
        // }
        // return result;
        return true;
    }

    @GetMapping("/logout")
    public boolean logout(HttpSession session) {
        // session.invalidate();
        return true;
    }

    @GetMapping("/existed")
    public boolean userExisted(@RequestParam String username) {
        // return userService.userExisted(username);
        return false;
    }

    @GetMapping("/get")
    public ProfileModel getUserProfile(HttpSession session) {
        // String userid = (String)session.getAttribute("user");
        // return userService.getUserProfile(userid);
        return new ProfileModel("123456789", "Koishi", "zsb_sjtu@sjtu.edu.cn", null, null, 0L);
    }

    @GetMapping("/getsim")
    public SimplifiedProfileModel getSimplifiedProfile(@RequestParam String userid) {
        // return userService.getSimplifiedProfile(userid);
        return new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null);
    }

    @GetMapping("/getintm")
    public List<SimplifiedProfileModel> getIntimateUsers(HttpSession session) {
        // String userid = (String)session.getAttribute("user");
        // return userService.getIntimateUsers(userid);
        List<SimplifiedProfileModel> simplifiedProfiles = new ArrayList<>();
        simplifiedProfiles.add(new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null));
        simplifiedProfiles.add(new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null));
        simplifiedProfiles.add(new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null));
        simplifiedProfiles.add(new SimplifiedProfileModel("12345678", "Koishi_Plus", null, null));
        return simplifiedProfiles;
    }

    @GetMapping("/verify/pwd")
    public boolean verifyPassword(@RequestParam String password, HttpSession session) {
        // String userid = (String)session.getAttribute("user");
        // return userService.verifyPassword(userid, password);
        return true;
    }

    @GetMapping("/verify/admin")
    public boolean adminVerify(@RequestParam String verifyKey, HttpSession session) {
        // String userid = (String)session.getAttribute("user");
        // return userService.verifyPassword(userid, password);
        return true;
    }

    @PostMapping("/add")
    public boolean addUser(@RequestBody UserModel user) {
        // return userService.addUser(user);
        return true;
    }

    @PutMapping("/profile/update")
    public boolean updateProfile(@RequestBody ProfileModel profile, HttpSession session) {
        // String userid = (String)session.getAttribute("user");
        // if(!userid.equals(profile.getUserid())) {
        //     throw new ForbiddenException("无权修改该用户信息");
        // }
        // return userService.updateProfile(profile);
        return true;
    }

    @PutMapping("pwd")
    public boolean updatePassword(@RequestParam String password, HttpSession session) {
        // String userid = (String)session.getAttribute("user");
        // return userService.updatePassword(userid, password);
        return true;
    }
}
