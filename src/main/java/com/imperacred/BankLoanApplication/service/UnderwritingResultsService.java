package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;

public interface UnderwritingResultsService {

	 UnderwritingResultsDTO performUnderwriting(Integer leadId);
}
