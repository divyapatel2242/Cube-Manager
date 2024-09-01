package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.Creds;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cube-manage")
public class CubeDashBoardController {

    @PostMapping("/dashboard")
    public String getDashboard(@ModelAttribute Creds creds){
        if(creds.getUserName().equalsIgnoreCase("divya") && creds.getPassWord().equalsIgnoreCase("divya"))
            return "dashboard";
        else
            return "login";
    }

}
