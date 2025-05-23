package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.DisbursementsDTO;

public interface DisbursementsService {
	
    DisbursementsDTO disburseLoan(DisbursementsDTO disbursementsDTO);

	void generateOtpForDisbursement(Integer leadsId);

	boolean verifyOtpForDisbursement(Integer leadsId, String otp);

	void resendOtpForDisbursement(Integer leadsId);
}


