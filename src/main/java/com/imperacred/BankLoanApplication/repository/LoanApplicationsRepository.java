package com.imperacred.BankLoanApplication.repository;

import com.imperacred.BankLoanApplication.model.LoanApplications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationsRepository extends JpaRepository<LoanApplications, Integer> {
}