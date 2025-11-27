package com.college.attendance.Attendance.API.filter;

import com.college.attendance.Attendance.API.entities.User;
import com.college.attendance.Attendance.API.repositories.UserRepository;
import com.college.attendance.Attendance.API.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.replace("Bearer ", "");
        var jwt = jwtService.parseToken(token);


        if (jwt == null || !jwt.isValid()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var userId = jwt.getUserId();


            if (userId == null) {
                System.err.println("JWT ERROR: User ID is null in token claims.");
                filterChain.doFilter(request, response);
                return;
            }

            var role = jwt.getRole();


            User user = userRepository.findById(userId).orElse(null);


            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getStudentCode())
                    .password(user.getPassword())
                    .authorities("ROLE_" + role)
                    .build();

            var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. مواصلة سلسلة الفلترة (النجاح)
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 5. في حالة فشل التحويل (مثل Role.valueOf) أو أي خطأ آخر
            System.err.println("Authentication failure after successful JWT parsing: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
    }
}