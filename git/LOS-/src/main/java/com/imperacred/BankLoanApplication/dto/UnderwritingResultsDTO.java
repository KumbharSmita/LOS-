package com.imperacred.BankLoanApplication.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingResultsDTO {

	private Integer resultId; 
    private Integer leadsId;
    
    private String riskRating;
    private BigDecimal approvedAmount;
    private String decision;
    private String underwriterNotes;
    private LocalDateTime evaluatedAt;
    private Integer agentId;

    private BigDecimal rateOfInterest;

    private Integer tenureMonths;

}
