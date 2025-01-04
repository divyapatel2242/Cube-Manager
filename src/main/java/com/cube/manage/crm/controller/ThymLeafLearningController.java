package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.Creds;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/*
 * @auther
 * Divya Patel
 * */

@Controller
@RequestMapping
public class ThymLeafLearningController {

    @PostMapping("/thym/dashboard")
    public String getDashboard(@ModelAttribute Creds creds){
        if(creds.getUsername().equalsIgnoreCase("divya") && creds.getPassword().equalsIgnoreCase("divya"))
            return "dashboard";
        else
            return "login";
    }

}

