package org.example.rgybackend.Controller;


import com.alibaba.fastjson2.JSONObject;
import org.example.rgybackend.Model.CrisisModel;
import org.example.rgybackend.Service.CrisisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crisis")
public class CrisisController {
    @Autowired
    private CrisisService crisisService;

    @GetMapping("/list")
    public List<CrisisModel> listCrisis() {
        return crisisService.getAllCrisis();
    }

    @GetMapping("/listAuditing")
    public List<CrisisModel> listCrisisAuditing() {
        return crisisService.getAllCrisisAuditing();
    }

    @GetMapping("/listUser/{userid}")
    public List<CrisisModel> listCrisisByUser(@PathVariable String userid) {
        return crisisService.getCrisisByUser(userid);
    }

    @PostMapping("/confirm")
    public void confirmCrisis(@RequestBody String json) {
        JSONObject obj = JSONObject.parseObject(json);
        int crisisid = obj.getIntValue("crisisid");
        crisisService.saveCrisis(crisisid);
    }


    @PostMapping("/delete")
    public void deleteCrisisAuditing(@RequestBody String json) {
        JSONObject obj = JSONObject.parseObject(json);
        int crisisid = obj.getIntValue("crisisid");
        crisisService.deleteCrisisAuditing(crisisid);
    }


}
