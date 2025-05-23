package com.imperacred.BankLoanApplication.service;

public interface OtpService {
	 String generateOtpForLead(Integer leadsId, String otpType);
	    boolean verifyOtpForLead(Integer leadsId, String otpType, String inputOtp);
	    String resendOtpForLead(Integer leadsId, String otpType);
}
