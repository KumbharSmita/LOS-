
package com.imperacred.BankLoanApplication.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.model.UserRegistration;
import com.imperacred.BankLoanApplication.repository.UserRegistrationRepository;
import com.imperacred.BankLoanApplication.service.UserLoginService;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private static final Logger logger = LogManager.getLogger(UserLoginServiceImpl.class);

    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Override
    public UserRegistration validateUserLogin(String email, String password) {
        logger.info("Attempting login for email: {}", email);

        UserRegistration user = userRegistrationRepository.findByEmail(email);

        if (user == null) {
            logger.warn("Login failed: No user found with email: {}", email);
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getPassword().equals(password)) {
            logger.warn("Login failed: Password mismatch for email: {}", email);
            throw new RuntimeException("Invalid credentials");
        }

        logger.info("Login successful for email: {}", email);
        return user;
    }
    
    @Override
    public UserRegistration getUserByEmail(String email) {
        return userRegistrationRepository.findByEmail(email);
    }

}
