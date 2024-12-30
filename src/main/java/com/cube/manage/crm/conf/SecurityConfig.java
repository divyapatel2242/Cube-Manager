package com.cube.manage.crm.conf;

import com.cube.manage.crm.security.SpringSecurityUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SpringSecurityUserDetailsService userDetailsService;

//    @Autowired
//    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        //Google/GitHub Login Support Config

//        return security.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//                .oauth2Login(Customizer.withDefaults())
//                .build();


        //ConventionalSecurityConfig
        return security.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> {
                            try {
                                request
                                        .antMatchers("/login","/logout", "/register/user")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                                        .and().logout().permitAll();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
//                .httpBasic().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).and()
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //for enable jwt
//                .formLogin(Customizer.withDefaults())
                .build();
//        security.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
