package com.ead.course.configs.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class JwtProvider {

   Logger log = LogManager.getLogger(JwtProvider.class);
   @Value("${ead.auth.jwtSecret}")
   private String jwtSecret;

   //extrai parte do token
   public String getSubjectJwt(String token){
     return Jwts.parserBuilder()
             .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))).build()
             .parseClaimsJws(token)
             .getBody()
             .getSubject();
   }

   //metodo generico que extrai informações do web token, neste caso as roles
   public String getClaimNameJwt(String token, String claimName){
       return Jwts.parserBuilder()
               .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))).build()
               .parseClaimsJws(token)
               .getBody()
               .get(claimName)
               .toString();
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
