package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadVerificationResponseDTO {
    private String message;
    private Integer leadsId;
    private Integer agentId;
    private LocalDateTime assignedAt;
    private LocalDateTime expectedContactTime;
}
