package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.Creds;
import com.cube.manage.crm.request.UserRegisterRequest;
import com.cube.manage.crm.service.CustomerService;
import com.cube.manage.crm.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model){
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Creds creds, Model model){
        if(loginService.validate(creds)){
            return ResponseEntity.ok("Ok");
        }
        model.addAttribute("error", "Invalid username or password.");
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "/register/user", method = RequestMethod.GET)
    public String getRegisterUser(Model model){
        return "userRegistration";
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.isInvalidateHttpSession();
        securityContextLogoutHandler.setClearAuthentication(true);
        securityContextLogoutHandler.setInvalidateHttpSession(true);
        SecurityContextHolder.clearContext();
        securityContextLogoutHandler.setSecurityContextRepository(null);
        return ResponseEntity.ok("Ok");
    }

}