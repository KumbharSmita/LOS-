package com.imperacred.BankLoanApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imperacred.BankLoanApplication.model.CreditScores;
@Repository
public interface CreditScoresRepository extends JpaRepository<CreditScores,Integer>{

	
	    Optional<CreditScores> findByApplicationId(Integer application_id);
	}



