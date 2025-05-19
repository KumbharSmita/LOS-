package com.imperacred.BankLoanApplication.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LoanAgreementDTO {
    private Integer agreementId;
    private Integer leadsId;
    private Integer sanctionId;
    private LocalDate agreementDate;
    private String agreementStatus;
    private String eSignatureUrl;
    private String signedBy;
    private LocalDateTime signedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Required for email service
    private String applicantName;
    private String applicantEmail;
    private BigDecimal loanAmount;
}
