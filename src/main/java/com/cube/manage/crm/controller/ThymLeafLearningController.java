package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.Creds;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ThymLeafLearningController {

    @GetMapping("/login")
    public String getLogin(Model module){
        module.addAttribute("creds", new Creds());
        return "login";
    }

    @PostMapping("/dashboard")
    public String getDashboard(@ModelAttribute Creds creds){
        if(creds.getUserName().equalsIgnoreCase("divya") && creds.getPassWord().equalsIgnoreCase("divya"))
            return "dashboard";
        else
            return "login";
    }

    @PostMapping("/logout")
    public String getLogout(Model module){
        module.addAttribute("creds", new Creds());
        return "login";
    }

}

