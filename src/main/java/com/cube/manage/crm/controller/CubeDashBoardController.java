package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.Creds;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CubeDashBoardController {

    @GetMapping("/dashboard")
    public String getDashboard(){
        return "dashboard";
    }

}
