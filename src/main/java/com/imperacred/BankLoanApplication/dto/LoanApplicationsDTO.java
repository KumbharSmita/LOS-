package com.imperacred.BankLoanApplication.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationsDTO {
    private Integer application_id;
    private Integer leads_id;
    private String loan_type;
    private Double amount;
    private Integer tenure_months;
    private String purpose;
    private String status;
    private LocalDateTime submitted_at;
}