//package com.cube.manage.crm.conf;
//
//
//import com.cube.manage.crm.security.JWTService;
//import com.cube.manage.crm.security.SpringSecurityUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Objects;
//
//@Component
//public class JWTFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String token = request.getHeader("Authorization");
//        String userName = null;
//
//        if(Objects.nonNull(token)){
//            token = token.substring(7);
//            userName = jwtService.extractUserNameFromToken(token);
//        }
//
//        if(Objects.nonNull(userName) && SecurityContextHolder.getContext().getAuthentication() == null){
//            UserDetails userDetails = applicationContext.getBean(SpringSecurityUserDetailsService.class).loadUserByUsername(userName);
//            if(jwtService.validateToken(token, userDetails)){
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request,response);
//
//    }
//}
