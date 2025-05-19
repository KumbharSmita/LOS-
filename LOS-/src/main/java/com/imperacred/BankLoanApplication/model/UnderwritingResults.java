package com.imperacred.BankLoanApplication.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "underwriting_results")

public class UnderwritingResults {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "result_id")
	private Integer resultId;

	@Column(name = "leads_id", nullable = false)
	private Integer leadsId; 
	private String riskRating;
	private BigDecimal approvedAmount;
	private String decision; // APPROVED, REJECTED, CONDITIONAL
	private String underwriterNotes;
	private LocalDateTime evaluatedAt;

}
