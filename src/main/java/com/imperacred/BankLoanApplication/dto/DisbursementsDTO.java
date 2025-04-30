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

	
	    private int applicationId;
	    private BigDecimal disbursedAmount;
	    private BigDecimal processingFee;
	    private BigDecimal actualAmountCredited;
	    private String bankAccount;
	    private String utrNumber;
	    private LocalDateTime disbursedAt;
	    private String status;


	}

	

