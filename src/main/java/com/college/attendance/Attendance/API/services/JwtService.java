package com.college.attendance.Attendance.API.services;

import com.college.attendance.Attendance.API.config.JwtConfig;
import com.college.attendance.Attendance.API.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessTokens(User user){
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshTokens(User user){
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpiration) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("name" , user.getName())
                .add("email" , user.getEmail())
                .add("role" , user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();
        String tokenValue = Jwts.builder()
                .claims(claims)
                .signWith(jwtConfig.getSecretKey())
                .compact();

        return new Jwt(claims , jwtConfig.getSecretKey() , tokenValue);
    }



    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Jwt parseToken(String token){
        try{
            var claims =getClaims(token);
            return new Jwt(claims , jwtConfig.getSecretKey() , token);
        } catch (JwtException e) {
            System.err.println("JWT ERROR: " + e.getMessage());
            return null ;
        }
    }

}
