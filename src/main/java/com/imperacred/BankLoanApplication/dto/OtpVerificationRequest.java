package com.imperacred.BankLoanApplication.dto;

import lombok.Data;

@Data

public class OtpVerificationRequest {
    private Integer leadsId;
    private String otpValue;

    // Getters and Setters
}

