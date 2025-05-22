
package com.imperacred.BankLoanApplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.UserLoginDTO;
import com.imperacred.BankLoanApplication.model.UserRegistration;
import com.imperacred.BankLoanApplication.service.UserLoginService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user-login")
public class UserLoginController {

    private static final Logger logger = LogManager.getLogger(UserLoginController.class);

    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        logger.info("Login attempt for email: {}", loginDTO.getEmail());
        try {
            UserRegistration user = userLoginService.validateUserLogin(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok("Login successful for: " + user.getEmail());
        } catch (RuntimeException e) {
            logger.warn("Login failed: {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
    
    @GetMapping("/get-by-email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        logger.info("Fetching user registration by email: {}", email);
        try {
            UserRegistration user = userLoginService.getUserByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (Exception e) {
            logger.error("Error fetching user by email: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Server error");
        }
    }
}
