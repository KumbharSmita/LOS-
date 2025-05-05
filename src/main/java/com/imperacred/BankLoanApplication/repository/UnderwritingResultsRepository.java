package com.imperacred.BankLoanApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.UnderwritingResults;

public interface UnderwritingResultsRepository extends JpaRepository<UnderwritingResults, Integer> {
}

