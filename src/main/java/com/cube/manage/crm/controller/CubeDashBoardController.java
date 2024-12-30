package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.Creds;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class CubeDashBoardController {

    @GetMapping("/dashboard")
    public String getDashboard(Model model){
        return "dashboard";
    }

}
