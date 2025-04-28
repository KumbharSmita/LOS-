package com.imperacred.BankLoanApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.Documents;

public interface DocumentsRepository extends JpaRepository<Documents, Integer> {
}