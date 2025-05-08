package com.imperacred.BankLoanApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credit_scores")
public class CreditScores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Leads ID cannot be blank")
    @Column(name = "leads_id", nullable = false)
    private int leadsId;

    @NotBlank(message = "Bureau cannot be blank")
    @Column(name = "bureau", length = 255)
    private String bureau;

    @Min(value = 300, message = "Score must be >= 300")
    @Max(value = 900, message = "Score must be <= 900")
    @Column(name = "score")
    private int score;

    @NotBlank(message = "Risk category cannot be blank")
    @Column(name = "risk")
    private String risk;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;
}
