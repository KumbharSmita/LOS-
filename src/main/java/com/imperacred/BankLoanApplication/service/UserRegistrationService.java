package com.imperacred.BankLoanApplication.service;

import java.util.List;
import com.imperacred.BankLoanApplication.dto.UserRegistrationDTO;

public interface UserRegistrationService {
    UserRegistrationDTO createUser(UserRegistrationDTO userDto);
    UserRegistrationDTO getUserById(Integer id);
    List<UserRegistrationDTO> getAllUsers();
    UserRegistrationDTO updateUser(Integer id, UserRegistrationDTO userDto);
    void deleteUser(Integer id);
    
 
}
