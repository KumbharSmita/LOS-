package com.imperacred.BankLoanApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.Disbursements;

public interface DisbursementsRepository extends JpaRepository<Disbursements,Integer> {

	Optional<Disbursements> findByApplicationId(int applicationId);

	
}
