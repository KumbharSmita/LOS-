package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.UserRegistrationDTO;
import com.imperacred.BankLoanApplication.service.UserRegistrationService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserRegistrationController {

    private static final Logger logger = LogManager.getLogger(UserRegistrationController.class);

    @Autowired
    private UserRegistrationService userService;

    @PostMapping("/register")
    public UserRegistrationDTO createUser(@RequestBody UserRegistrationDTO userRegistrationDto) {
        logger.info("Received request to register user with email: {}", userRegistrationDto.getEmail());
        UserRegistrationDTO createdUser = userService.createUser(userRegistrationDto);
        logger.info("User registered successfully with ID: {}", createdUser.getUserRegistrationId());
        return createdUser;
    }

    @GetMapping("/{id}")
    public UserRegistrationDTO getUser(@PathVariable Integer id) {
        logger.debug("Received request to get user with ID: {}", id);
        UserRegistrationDTO user = userService.getUserById(id);
        logger.info("User retrieved with ID: {}", id);
        return user;
    }

    @GetMapping
    public List<UserRegistrationDTO> getAllUsers() {
        logger.debug("Received request to get all users");
        List<UserRegistrationDTO> users = userService.getAllUsers();
        logger.info("Total users fetched: {}", users.size());
        return users;
    }

    @PutMapping("/{id}")
    public UserRegistrationDTO updateUser(@PathVariable Integer id, @RequestBody UserRegistrationDTO userDto) {
        logger.info("Received request to update user with ID: {}", id);
        UserRegistrationDTO updatedUser = userService.updateUser(id, userDto);
        logger.info("User updated successfully with ID: {}", id);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        logger.info("Received request to delete user with ID: {}", id);
        userService.deleteUser(id);
        logger.info("User deleted successfully with ID: {}", id);
    }
}