package com.lalit.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    public String extractEmail(String token){
        return extractAllClaims(token)
                .getSubject();
    }

    public boolean isTokenValid(String token){
        try{
            extractAllClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keysBytes= Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keysBytes);
    }

    public String extractRole(String token){

        return extractAllClaims(token)
                .get("role", String.class);
    }
}
