package com.imperacred.BankLoanApplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer application_id;

    @Column(name = "leads_id")
    private Integer leads_id;

    @Column(name = "loan_type")
    private String loan_type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "tenure_months")
    private Integer tenure_months;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "status")
    private String status;

    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submitted_at;
}