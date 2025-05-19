package com.imperacred.BankLoanApplication.service;

/**
 * Interface for sending various types of emails related to the loan application process.
 */
public interface EmailService {

    // Generic email sender
    void sendEmail(String toEmail, String subject, String message);

    // OTP email sender
    void sendOtpEmail(String email, String otp);

    // Send when documents are uploaded successfully
    void sendDocumentUploadConfirmation(String email, String applicantName);

    // Send when loan is approved
    void sendLoanApprovalEmail(String email, String applicantName, String loanAmount, String approvedDate);

    // Send when loan is rejected
    void sendLoanRejectionEmail(String email, String applicantName, String reason);
}
