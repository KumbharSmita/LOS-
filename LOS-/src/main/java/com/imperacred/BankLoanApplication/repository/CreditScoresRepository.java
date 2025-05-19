package com.imperacred.BankLoanApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.imperacred.BankLoanApplication.model.CreditScores;

public interface CreditScoresRepository extends JpaRepository<CreditScores, Integer> {
    // Optional: add custom methods if needed
}