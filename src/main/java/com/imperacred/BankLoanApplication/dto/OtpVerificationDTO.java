package com.imperacred.BankLoanApplication.dto;

import lombok.Data;

@Data
public class OtpVerificationDTO {
    private Integer leads_id;  // ID of the lead for which OTP verification is being done
    private String otp_value;        // OTP entered by the user for verification
}