package com.imperacred.BankLoanApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imperacred.BankLoanApplication.model.Kyc;

@Repository
public interface KycRepository extends JpaRepository<Kyc, Integer> {
    Optional<Kyc> findByLead_LeadId(Integer leadId);
}

