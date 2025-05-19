package com.imperacred.BankLoanApplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "loan_agreement")
public class LoanAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
