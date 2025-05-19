package com.imperacred.BankLoanApplication.repository;

import com.imperacred.BankLoanApplication.model.SanctionLetter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanctionLetterRepository extends JpaRepository<SanctionLetter, Integer> {
}