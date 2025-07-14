package com.hbl.authserver.util;


import com.hbl.authserver.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final static String SECRET_KEY = "usama12nckeyrjkhjhdfkcwhekfjwowjwjkwjfl3";
    private final static long EXPIRE_IN = 8640000;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(AppUser appUser, List<String> authorities) {
        Date now = new Date();
        Date expiryTime = new Date(now.getTime() + EXPIRE_IN);
        return Jwts.builder()
                .setSubject(appUser.getUuid())
                .claim("username", appUser.getUsername())
                .claim("authorities", authorities)
                .setIssuedAt(now)
                .setExpiration(expiryTime)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String getUserUuidFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims= Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object rawAuthorities = claims.get("authorities");

        if (rawAuthorities instanceof List<?> list){
            return list.stream()
                    .filter(item -> item instanceof String)
                    .map(item ->new SimpleGrantedAuthority((String) item))
                    .map(authority -> (GrantedAuthority) authority)
                    .toList();
        }
        return Collections.emptyList();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.error("Error in validation" + e.getMessage());
        }
        return false;
    }

}
