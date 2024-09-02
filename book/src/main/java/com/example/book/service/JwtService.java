package com.example.book.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JwtService {

    private final String SECRET_KEY = "2679C77505DECF5E84945BDBBEC711D0E59041BE4F0B00297EF5383BED7FEDBA";
    private final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(30);// 30 minutes


    public String generateToken(UserDetails userDetails){
        log.info("gen token username {}",userDetails.getUsername());
        Map<String,String> claims=new HashMap<>();
        claims.put("iss", "SecureAPI");
        claims.put("aud","Authorized Users");
        claims.put("roles",userDetails.getAuthorities().toString());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(EXPIRATION_TIME)))
                .signWith(generateKey())
                .compact();

    }

   private SecretKey generateKey(){
        byte[] decodedKey=Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
   }

    public boolean validateToken(String token){
        Claims claims=getClaims(token);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    private Claims getClaims(String token){
        Claims claims=Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;

    }
    public  String extractUsername(String token){
        Claims claims=getClaims(token);
        return claims.getSubject();
    }




}
