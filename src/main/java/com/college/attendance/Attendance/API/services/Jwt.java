package com.college.attendance.Attendance.API.services;

import com.college.attendance.Attendance.API.entities.Role;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.SecretKey;
import java.util.Date;

@Data
@AllArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey ;
    private final String tokenValue ;
    public boolean isValid(){
        return claims.getExpiration().after(new Date());
    }

    public Long getUserId(){
        try {
            String subject = claims.getSubject();
            if (subject == null) {
                return null;
            }

            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            System.err.println("JWT ID PARSING ERROR: Cannot convert subject to Long: " + claims.getSubject());
            return null;
        }
    }
    public Role getRole(){
        return Role.valueOf(claims.get("role" , String.class));
    }

    @Override
    public String toString() {
        return this.tokenValue;
    }
}
