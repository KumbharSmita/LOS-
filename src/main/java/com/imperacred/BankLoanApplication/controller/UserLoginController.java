package com.imperacred.BankLoanApplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.UserLoginDTO;
import com.imperacred.BankLoanApplication.model.UserLogin;
import com.imperacred.BankLoanApplication.service.UserLoginService;

@RestController
@RequestMapping("/api/user-login")
public class UserLoginController {

    private static final Logger logger = LogManager.getLogger(UserLoginController.class);

    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/save")
    public ResponseEntity<UserLogin> saveUserLogin(@RequestBody UserLoginDTO userLoginDTO) {
        logger.info("Received request to save user login for email: {}", userLoginDTO.getEmail());
        try {
            UserLogin savedUser = userLoginService.saveUserLogin(userLoginDTO);
            logger.info("User login successfully saved for email: {}", savedUser.getEmail());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            logger.error("Error while saving user login: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<UserLogin> getUserLoginByEmail(@RequestParam String email) {
        logger.debug("Received request to fetch user login by email: {}", email);
        UserLogin userLogin = userLoginService.getUserLoginByEmail(email);
        if (userLogin != null) {
            logger.info("User login found for email: {}", email);
            return ResponseEntity.ok(userLogin);
        } else {
            logger.warn("User login not found for email: {}", email);
            return ResponseEntity.notFound().build();
        }
    }
}
