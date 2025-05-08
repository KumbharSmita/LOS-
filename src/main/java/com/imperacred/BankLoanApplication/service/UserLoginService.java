package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.UserLoginDTO;
import com.imperacred.BankLoanApplication.model.UserLogin;

public interface UserLoginService {
    UserLogin saveUserLogin(UserLoginDTO userLoginDTO);
    UserLogin getUserLoginByEmail(String email);
}
