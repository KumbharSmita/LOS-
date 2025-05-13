package com.imperacred.BankLoanApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.imperacred.BankLoanApplication.model.UserLogin;

public interface UserLoginRepository extends JpaRepository<UserLogin, Integer> {
    UserLogin findByEmail(String email);
}
