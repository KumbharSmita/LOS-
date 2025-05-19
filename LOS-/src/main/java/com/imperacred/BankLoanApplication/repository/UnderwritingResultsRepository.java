package com.imperacred.BankLoanApplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;

public interface UnderwritingResultsRepository extends JpaRepository<UnderwritingResults,Integer>{

	
	List<UnderwritingResults> findByLeadsId(Integer leadsId);

}

