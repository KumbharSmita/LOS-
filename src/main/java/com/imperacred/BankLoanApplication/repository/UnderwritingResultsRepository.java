package com.imperacred.BankLoanApplication.repository;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;

public interface UnderwritingResultsRepository extends JpaRepository<UnderwritingResults,Integer>{

	
	List<UnderwritingResults> findByLeadsId(Integer leadsId);
=======
import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> origin/feature2

import com.imperacred.BankLoanApplication.model.UnderwritingResults;

public interface UnderwritingResultsRepository extends JpaRepository<UnderwritingResults, Integer> {
}

