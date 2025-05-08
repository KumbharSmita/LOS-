package com.imperacred.BankLoanApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.imperacred.BankLoanApplication.model.UserRegistration;

public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Integer> {
}
