package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.Creds;
import com.cube.manage.crm.request.UserRegisterRequest;
import com.cube.manage.crm.service.CustomerService;
import com.cube.manage.crm.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Creds creds){
        if(true){
            return ResponseEntity.ok("Ok");
        }
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "/register/user", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequest userRegisterRequest, Model model){
        try {
            customerService.registerUser(userRegisterRequest);
            return ResponseEntity.ok("Ok");
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

//    @PostMapping(value = "/logout")
//    public ResponseEntity<String> logout() {
//        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
//        securityContextLogoutHandler.isInvalidateHttpSession();
//        securityContextLogoutHandler.setClearAuthentication(true);
//        securityContextLogoutHandler.setInvalidateHttpSession(true);
//        SecurityContextHolder.clearContext();
//        securityContextLogoutHandler.setSecurityContextRepository(null);
//        return ResponseEntity.ok("Ok");
//    }

    @PostMapping("/is-autherized")
    public ResponseEntity<String> checkAutherization(){
//        if(loginService.isAutherized())
            return ResponseEntity.ok("Authenticated");
//        else
//            return ResponseEntity.ok("Not Authenticated");
    }

}