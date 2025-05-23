package com.imperacred.BankLoanApplication.service;

public interface EmailService {

    // Method to send a general email with subject and message content
    void sendEmail(String toEmail, String subject, String message);

    // Method to send OTP email
    void sendOtpEmail(String email, String otp);

	void sendSimpleEmail(String email, String string, String body);
	
	void sendDisbursementOtpEmail(String email, String name, String otp, boolean isResend);

    
}

