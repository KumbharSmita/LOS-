package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.LeadVerificationResponseDTO;
import com.imperacred.BankLoanApplication.dto.LeadsDTO;

public interface LeadsService {

	LeadsDTO createLead(LeadsDTO leadDTO);
    
    // Method to verify OTP for a given lead ID
    LeadVerificationResponseDTO verifyOtp(Integer leads_id, String otpValue);

    // Method to resend OTP for an existing lead
    String resendOtp(String email);

	
}


