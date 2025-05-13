package com.imperacred.BankLoanApplication.dto;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnderwritingResultsDTO {


    private Integer leadsId;
    private String riskRating;
    private BigDecimal approvedAmount;
    private String decision;
    private String underwriterNotes;
    private LocalDateTime evaluatedAt;

=======
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
>>>>>>> origin/feature2
}
