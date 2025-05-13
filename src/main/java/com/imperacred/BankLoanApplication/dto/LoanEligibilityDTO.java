package com.imperacred.BankLoanApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LoanEligibilityDTO {
    private int score;
    private BigDecimal eligibleAmount;
    private String risk;
}
