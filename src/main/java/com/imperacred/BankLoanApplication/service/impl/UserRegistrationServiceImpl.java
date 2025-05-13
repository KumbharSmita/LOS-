package com.imperacred.BankLoanApplication.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.UserRegistrationDTO;
import com.imperacred.BankLoanApplication.model.UserRegistration;
import com.imperacred.BankLoanApplication.repository.UserRegistrationRepository;
import com.imperacred.BankLoanApplication.service.UserRegistrationService;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger logger = LogManager.getLogger(UserRegistrationServiceImpl.class);

    @Autowired
    private UserRegistrationRepository userRepo;

    private UserRegistrationDTO convertToDto(UserRegistration user) {
        return new UserRegistrationDTO(
            user.getUser_registration_id(),
            user.getFull_name(),
            user.getEmail(),
            user.getPassword(),
            user.getContactno()
        );
    }

    private UserRegistration convertToEntity(UserRegistrationDTO dto) {
        return new UserRegistration(
            dto.getUser_registration_id(),
            dto.getFull_name(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getContactno()
        );
    }

    @Override
    public UserRegistrationDTO createUser(UserRegistrationDTO userDto) {
        logger.info("Creating user with email: {}", userDto.getEmail());

        // ✅ Validation: Check for duplicate email and contact number
        if (userRepo.existsByEmail(userDto.getEmail())) {
            logger.warn("Email already exists: {}", userDto.getEmail());
            throw new RuntimeException("Email already exists");
        }

        if (userRepo.existsByContactno(userDto.getContactno())) {
            logger.warn("Contact number already exists: {}", userDto.getContactno());
            throw new RuntimeException("Contact number already exists");
        }

        UserRegistration user = convertToEntity(userDto);
        UserRegistration savedUser = userRepo.save(user);
        logger.info("User created successfully with ID: {}", savedUser.getUser_registration_id());
        return convertToDto(savedUser);
    }

    @Override
    public UserRegistrationDTO getUserById(Integer id) {
        logger.debug("Fetching user by ID: {}", id);
        return userRepo.findById(id)
                .map(user -> {
                    logger.info("User found with ID: {}", id);
                    return convertToDto(user);
                })
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new RuntimeException("User not found");
                });
    }

    @Override
    public List<UserRegistrationDTO> getAllUsers() {
        logger.debug("Fetching all users");
        List<UserRegistrationDTO> users = userRepo.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        logger.info("Fetched {} users", users.size());
        return users;
    }

    @Override
    public UserRegistrationDTO updateUser(Integer id, UserRegistrationDTO userDto) {
        logger.info("Updating user with ID: {}", id);
        Optional<UserRegistration> optional = userRepo.findById(id);
        if (optional.isEmpty()) {
            logger.warn("User not found for update with ID: {}", id);
            throw new RuntimeException("User not found");
        }

        UserRegistration existingUser = optional.get();

        // ✅ Validation: Check for duplicate email (other than current user)
        if (!existingUser.getEmail().equals(userDto.getEmail()) &&
                userRepo.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // ✅ Validation: Check for duplicate contact number (other than current user)
        if (!existingUser.getContactno().equals(userDto.getContactno()) &&
                userRepo.existsByContactno(userDto.getContactno())) {
            throw new RuntimeException("Contact number already exists");
        }

        existingUser.setFull_name(userDto.getFull_name());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPassword(userDto.getPassword());
        existingUser.setContactno(userDto.getContactno());

        UserRegistration updatedUser = userRepo.save(existingUser);
        logger.info("User updated successfully with ID: {}", updatedUser.getUser_registration_id());
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        logger.info("Deleting user with ID: {}", id);
        userRepo.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }
}
