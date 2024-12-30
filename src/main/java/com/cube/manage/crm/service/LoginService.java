package com.cube.manage.crm.service;

import com.cube.manage.crm.request.Creds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    private JWTService jwtService;

    public boolean validate(Creds creds) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUserName(),creds.getPassWord()));
        if(authentication.isAuthenticated()){
            //return jwtService.generateToken(creds.getUserName());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Map<Character,Integer> getValue = new HashMap<>();
            return true;

        }
        return false;
    }
}
