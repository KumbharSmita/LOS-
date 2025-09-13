package com.imperacred.BankLoanApplication.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisbursementsDTO {

	
	    private Integer leadsId;
	    private BigDecimal approvedAmount;
	    private BigDecimal rateOfInterest;
	    private BigDecimal processingFee;
	    private BigDecimal disbursedAmount;
	    private String bankAccount;
	    private String utrNumber;
	    private LocalDateTime disbursedAt;
	    private String status;


	}

	

