package com.ead.authuser.configs.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class AuthenticationJwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtProvider jwtProvider;
  @Autowired
  private UserDetailsServiceImpl userDetailsService;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try{
        String jwtStr = getTokenHeader(request);
        if(jwtStr!= null && jwtProvider.validateJwt(jwtStr)){
          String userId = jwtProvider.getSubjectJwt(jwtStr);
          UserDetails userDetails = userDetailsService.loadUserById(UUID.fromString(userId));
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
