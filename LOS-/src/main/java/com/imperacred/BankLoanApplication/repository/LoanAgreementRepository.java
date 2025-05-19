
  package com.imperacred.BankLoanApplication.repository;
  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imperacred.BankLoanApplication.model.LoanAgreement;

@Repository
public interface LoanAgreementRepository extends JpaRepository<LoanAgreement, Integer> {
}
