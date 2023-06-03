package com.ead.authuser.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity// desligando as configurações dafault do spring security
@Configuration
public class WebSecurityConfig  {

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static  final String[]  AUTH_WHITLIST ={
        "/auth/**"
    };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .httpBasic()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .authorizeRequests()
            .requestMatchers(AUTH_WHITLIST).permitAll()
            .requestMatchers(HttpMethod.GET,"/users/**").hasRole("INSTRUCTOR")
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
           // .formLogin();
    return http.build();
  }
/*
  @Bean
    public UserDetailsService userDetailsService(AuthenticationManagerBuilder aut) throws Exception {
        aut
           .userDetailsService(userServiceImpl)
           .passwordEncoder(passwordEncoder());
        return aut.getDefaultUserDetailsService();

    }

 */

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }



}
