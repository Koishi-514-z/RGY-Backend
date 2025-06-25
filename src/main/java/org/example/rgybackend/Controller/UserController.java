package org.example.rgybackend.Controller;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Entity.Profile;
import org.example.rgybackend.Entity.User;
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
        return true;
    }

    @GetMapping("/existed")
    public boolean userExisted(@RequestParam String username) {
        return false;
    }

    @GetMapping("/get")
    public Profile getUserProfile(HttpSession session) {
        return new Profile("123456789", "Koishi", "zsb_sjtu@sjtu.edu.cn", null, null);
    }

    @GetMapping("/getintm")
    public List<Profile> getIntimateUsers() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile("123456789", "Koishi", "zsb_sjtu@sjtu.edu.cn", null, null));
        profiles.add(new Profile("123456789", "Koishi", "zsb_sjtu@sjtu.edu.cn", null, null));
        profiles.add(new Profile("123456789", "Koishi", "zsb_sjtu@sjtu.edu.cn", null, null));
        profiles.add(new Profile("123456789", "Koishi", "zsb_sjtu@sjtu.edu.cn", null, null));
        return profiles;
    }

    @GetMapping("/verify/pwd")
    public boolean verifyPassword(@RequestParam String password) {
        return true;
    }

    @PostMapping("/add")
    public boolean addUser(@RequestBody User user) {
        return true;
    }

    @PutMapping("/profile/update")
    public boolean updateProfile(@RequestBody Profile profile, HttpSession session) {
        String userid = (String)session.getAttribute("user");
        if(!userid.equals(profile.getUserid())) {
            throw new ForbiddenException("无权修改该用户信息");
        }
        return true;
    }

    @PutMapping("pwd")
    public boolean updatePassword(@RequestParam String password) {
        return true;
    }
}
