package com.cube.manage.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cube-manage/home")
public class HomeController {

    @GetMapping("/")
    public String getHomePage(){
        return "home";
    }

}
