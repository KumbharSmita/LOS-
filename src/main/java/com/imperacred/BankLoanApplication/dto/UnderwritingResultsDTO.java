package com.imperacred.BankLoanApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingResultsDTO {
    private Integer applicationId;
    private String riskRating;
    private BigDecimal approvedAmount;
    private String decision;
    private Integer creditScore; //  NEW
    private String email;        //  NEW
    private String underwriterNotes;
}
