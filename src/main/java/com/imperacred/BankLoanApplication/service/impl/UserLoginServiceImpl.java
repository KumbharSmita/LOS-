package com.imperacred.BankLoanApplication.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.UserLoginDTO;
import com.imperacred.BankLoanApplication.model.UserLogin;
import com.imperacred.BankLoanApplication.model.UserRegistration;
import com.imperacred.BankLoanApplication.repository.UserLoginRepository;
import com.imperacred.BankLoanApplication.repository.UserRegistrationRepository;
import com.imperacred.BankLoanApplication.service.UserLoginService;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private static final Logger logger = LogManager.getLogger(UserLoginServiceImpl.class);

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Override
    public UserLogin saveUserLogin(UserLoginDTO dto) {
        logger.info("Attempting to save user login for email: {}", dto.getEmail());

        UserRegistration userRegistration = userRegistrationRepository
            .findById(dto.getUser_registration_id())
            .orElseThrow(() -> {
                logger.error("UserRegistration not found with ID: {}", dto.getUser_registration_id());
                return new RuntimeException("UserRegistration not found with ID: " + dto.getUser_registration_id());
            });

        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(dto.getEmail());
        userLogin.setPassword(dto.getPassword());
//        userLogin.setUserRegistration(userRegistration);

        UserLogin savedLogin = userLoginRepository.save(userLogin);
        logger.info("User login saved successfully for email: {}", dto.getEmail());

        return savedLogin;
    }

    @Override
    public UserLogin getUserLoginByEmail(String email) {
        logger.debug("Fetching user login by email: {}", email);
        return userLoginRepository.findByEmail(email);
    }
}
