package com.imperacred.BankLoanApplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class UnderwritingResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultId;

    private Integer applicationId;

    private String riskRating;

    private BigDecimal approvedAmount;

    private String decision;

    @Column(columnDefinition = "TEXT")
    private String underwriterNotes;

    private LocalDateTime evaluatedAt;

    @PrePersist  //autofills evaluated at before insert
    public void prePersist() {
        this.evaluatedAt = LocalDateTime.now();
    }
}

