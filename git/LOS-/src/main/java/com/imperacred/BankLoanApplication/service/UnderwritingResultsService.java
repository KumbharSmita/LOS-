package com.imperacred.BankLoanApplication.service;

import java.math.BigDecimal;
import java.util.List;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;

import jakarta.servlet.http.HttpServletRequest;

public interface UnderwritingResultsService {

	UnderwritingResultsDTO performUnderwriting(
	        Integer leadsId, 
	        BigDecimal approvedAmount, 
	        BigDecimal rateOfInterest,     
	        Integer tenureMonths,           
	        HttpServletRequest request
	    );

	    UnderwritingResultsDTO getUnderwritingByLeadId(Integer leadsId);
	    List<UnderwritingResultsDTO> getAllUnderwritingResults();

		List<UnderwritingResultsDTO> getUnderwritingResultsByLoggedInAgent(HttpServletRequest request);
}

