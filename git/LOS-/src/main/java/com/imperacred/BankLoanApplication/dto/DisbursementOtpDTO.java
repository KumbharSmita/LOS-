package com.imperacred.BankLoanApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DisbursementOtpDTO {
	private Integer leadsId;
	 private String otpValue;
}
