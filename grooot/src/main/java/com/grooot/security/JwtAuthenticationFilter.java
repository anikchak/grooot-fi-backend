package com.grooot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.grooot.utility.JwtUtil;

import io.jsonwebtoken.Claims;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract and validate the JWT token from the request header.
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            // Validate the token 
            try {
                Claims claims = jwtUtil.extractClaims(accessToken);
                String userUUID = claims.get("userUUID", String.class);
                if (jwtUtil.isTokenExpired(accessToken)) {
                    String refreshToken = request.getHeader("Refresh-Token");
                    


            } 
        }catch (Exception e) {
                // TODO: handle exception
            }
        }
        
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
