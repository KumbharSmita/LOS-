package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.BorrowerSelectionDTO;
import com.imperacred.BankLoanApplication.dto.LeadsDTO;

public interface BorrowerSelectionService {
	BorrowerSelectionDTO confirmLoanSelection(Integer leadsId, BorrowerSelectionDTO selectionDTO);
}
