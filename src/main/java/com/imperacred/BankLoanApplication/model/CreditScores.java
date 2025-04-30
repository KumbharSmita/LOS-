package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="credit_scores")
public class CreditScores {
	
	@Id	
	private Integer id;
	@Column(name="application_id")
	private Integer applicationId;
	private String bereau;
	private Integer score;
	private LocalDateTime fatched_at;
	
}
