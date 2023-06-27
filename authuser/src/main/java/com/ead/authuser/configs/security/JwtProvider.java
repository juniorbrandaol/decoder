package com.ead.authuser.configs.security;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtProvider {

   @Value("${ead.auth.jwtSecret}")
   private String jwtSecret;
   @Value("${ead.auth.jwtExpirationMs}")
   private int jwtExpirationMs;

   //gerar token

   public String generateJwt(Authentication authentication){
       UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
       final String roles = userPrincipal.getAuthorities().stream()
               .map(role-> {return role.getAuthority(); } ).collect(Collectors.joining(","));
       return Jwts.builder()
               .setSubject((userPrincipal.getUserId().toString()))
               .claim("roles",roles)
               .setIssuedAt(new Date())
               .setExpiration(new Date( new Date().getTime()+jwtExpirationMs))
               .signWith(SignatureAlgorithm.HS512,jwtSecret)
               .compact();
   }

   //extrai parte do token

   public String getSubjectJwt(String token){
     return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
   }

   // valida o token

   public boolean validateJwt(String authToken){
      try{
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
        return true;
      }catch (SignatureException e){
        log.error("Invalid Jwt signature: {}", e.getMessage());
      }catch (MalformedJwtException e){
        log.error("Invalid Jwt token: {}", e.getMessage());
      }catch (ExpiredJwtException e){
        log.error("Jwt token is espired: {}", e.getMessage());
      }catch (UnsupportedJwtException e){
        log.error("Jwt token is unsupported: {}", e.getMessage());
      }catch (IllegalArgumentException e){
        log.error("Jwt claims string is empity signature: {}", e.getMessage());
      }
        return false;
   }

}
