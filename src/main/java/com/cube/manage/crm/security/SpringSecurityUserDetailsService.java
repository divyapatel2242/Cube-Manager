//package com.cube.manage.crm.security;
//
//import com.cube.manage.crm.entity.UserCreds;
//import com.cube.manage.crm.repository.CustomerCredsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
//@Service
//public class SpringSecurityUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private CustomerCredsRepository customerCredsRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserCreds userCreds = customerCredsRepository.findByUserName(username);
//        if(Objects.isNull(userCreds)){
//            throw new UsernameNotFoundException("User Not Found!");
//        }
//        return new UserPrinciple(userCreds);
//    }
//}
