package com.imperacred.BankLoanApplication.config;

import com.imperacred.BankLoanApplication.model.Role;
import com.imperacred.BankLoanApplication.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {

    @Autowired
    private JwtUtil jwtUtil;

    public String getLoggedInUserEmail(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        return token != null ? jwtUtil.extractEmail(token) : null;
    }

    
    public Role getLoggedInUserRole(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        return token != null ? jwtUtil.extractRole(token) : null;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }
}
