package com.college.attendance.Attendance.API.controller;

import com.college.attendance.Attendance.API.config.JwtConfig;
import com.college.attendance.Attendance.API.dtos.FixPasswordDto;
import com.college.attendance.Attendance.API.dtos.JwtResponse;
import com.college.attendance.Attendance.API.dtos.LoginRequestDto;
import com.college.attendance.Attendance.API.repositories.UserRepository;
import com.college.attendance.Attendance.API.services.JwtService;
import com.college.attendance.Attendance.API.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private JwtConfig jwtConfig;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Valid LoginRequestDto loginRequestDto,
            HttpServletResponse response){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getStudentCode(),
                        loginRequestDto.getPassword()
                )
        );
        var user = userRepository.findByStudentCode(loginRequestDto.getStudentCode()).get();

        var accessToken = jwtService.generateAccessTokens(user);
        var refreshToken = jwtService.generateRefreshTokens(user);

        var cookie = new Cookie("refreshToken" , refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken){
        var jwt = jwtService.parseToken(refreshToken);
        if(!jwt.isValid()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userId = jwt.getUserId();
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken =jwtService.generateAccessTokens(user);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }


}
