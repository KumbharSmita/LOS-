package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.BankDetailsDTO;

public interface BankDetailsService {
	 BankDetailsDTO saveBankDetails(Integer leadsId, BankDetailsDTO dto);

	BankDetailsDTO getBankDetailsByLeadId(Integer leadId);
}

