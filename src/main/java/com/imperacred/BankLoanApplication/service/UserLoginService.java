package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.UserLoginDTO;
import com.imperacred.BankLoanApplication.model.UserLogin;
import com.imperacred.BankLoanApplication.model.UserRegistration;

public interface UserLoginService {
  
    
    UserRegistration validateUserLogin(String email, String password);
    
    UserRegistration getUserByEmail(String email);


}
