package com.imperacred.BankLoanApplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OtpVerificationDTO {
	  private Integer leadsId;
	    private String otpValue;
}
