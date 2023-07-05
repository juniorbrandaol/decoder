package com.ead.authuser.configs.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
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
       SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
       return Jwts.builder()
               .setSubject((userPrincipal.getUserId().toString()))
               .claim("roles",roles)
               .setIssuedAt(new Date())
               .setExpiration(new Date( new Date().getTime()+jwtExpirationMs))
               .signWith(secretKey,SignatureAlgorithm.HS512)
               .compact();
   }

   //extrai parte do token
   public String getSubjectJwt(String token){
     return Jwts.parserBuilder()
             .setSigningKey( Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))).build()
             .parseClaimsJws(token).getBody().getSubject();
   }

   // valida o token
   public boolean validateJwt(String authToken){
      try{
        Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))).build()
                .parseClaimsJws(authToken);
        return true;
      }catch (SecurityException e){
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
