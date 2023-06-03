package com.ead.authuser.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity// desligando as configurações dafault do spring security
@Configuration
public class WebSecurityConfig {

    private static  final String[]  AUTH_WHITLIST ={
        "/auth/**"
    };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .httpBasic()
            .and()
            .authorizeRequests()
            .requestMatchers(AUTH_WHITLIST).permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .formLogin();
    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    UserDetails user = User.withUsername("admin")
            .password(passwordEncoder().encode("1224"))
            .roles("ADMIN")
            .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
