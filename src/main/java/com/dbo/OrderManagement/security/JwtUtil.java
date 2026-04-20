package com.dbo.OrderManagement.security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    //private final String SECRET = "mySuperSecureKey1234567890123456";
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username,String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(key)
                .compact();
    }

    public String extractUserName(String token){

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractUserRole(String token){

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .get("role",String.class);
    }

    public boolean validateToken(String token,String username){
        return extractUserName(token).equals(username);
    }




}
