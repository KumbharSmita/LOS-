package com.imperacred.BankLoanApplication.service;

import java.math.BigDecimal;
import java.util.List;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;

public interface UnderwritingResultsService {

	UnderwritingResultsDTO performUnderwriting(Integer leadId, BigDecimal approvedAmount);

	UnderwritingResultsDTO getUnderwritingByLeadId(Integer leadsId);

	List<UnderwritingResults> getAllUnderwritingResults();

}
