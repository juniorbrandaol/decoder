package com.ead.notfication.configs.security;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity
@EnableWebSecurity// desligando as configurações dafault do spring security
@Configuration
public class WebSecurityConfig {

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public AuthenticationJwtFilter authenticationJwtFilter(){
      return new AuthenticationJwtFilter();
    }

    //define a hierarquia das roles
    @Bean
    public RoleHierarchy roleHierarchy(){
      RoleHierarchyImpl roleHierarchyimpl = new RoleHierarchyImpl();
      String hierarchy = "ROLE_ADMIN > ROLE_INSTRUCTOR \n ROLE_INSTRUCTOR > ROLE_STUDENT \n  ROLE_STUDENT > ROLE_USER ";
      roleHierarchyimpl.setHierarchy(hierarchy);
      return roleHierarchyimpl;
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler expressionHandler(){
      DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
      expressionHandler.setRoleHierarchy(roleHierarchy());
      return expressionHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
            .exceptionHandling()
              .accessDeniedHandler(accessDeniedHandler)
              .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
      http.addFilterBefore(authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
      return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
