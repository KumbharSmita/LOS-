package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;

public interface LeadsService {

    // Method to create a lead and send OTP
    void createLead(LeadsDTO leadDTO);

    // Method to verify OTP for a given lead ID
    String verifyOtp(Integer leads_id, String otpValue);

    // Method to resend OTP for an existing lead
    String resendOtp(String email);
}
