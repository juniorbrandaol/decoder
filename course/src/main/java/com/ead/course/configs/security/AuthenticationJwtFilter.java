package com.ead.course.configs.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

//classe que filtra o token e libera acesso. Usaremos a classe OncePerRequestFilter
public class AuthenticationJwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtProvider jwtProvider;

  Logger logger = LogManager.getLogger(AuthenticationJwtFilter.class);
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try{
        String jwtStr = getTokenHeader(request);
        if(jwtStr!= null && jwtProvider.validateJwt(jwtStr)){
          String userId = jwtProvider.getSubjectJwt(jwtStr);
          String rolesStr = jwtProvider.getClaimNameJwt(jwtStr,"roles");
          UserDetails userDetails = UserDetailsImpl.build(UUID.fromString(userId),rolesStr);
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,null,userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }catch (Exception e){
        logger.error("cannot set user Authentication {}",e);
      }
      filterChain.doFilter(request,response);
  }

  private String getTokenHeader(HttpServletRequest request){
      String headerAuth = request.getHeader("Authorization");
      if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
        return headerAuth.substring(7, headerAuth.length());
      }
      return null;
  }


}
