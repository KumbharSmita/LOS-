package com.imperacred.BankLoanApplication.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
	@Table(name = "emi")
	public class Emi {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer emi_id;

	    private Integer applicationId;
	    private Integer schedule_id;
	    private BigDecimal emi_amount;
	    private LocalDate due_date;
	    private LocalDate paid_date;
	    private String status;
	    private LocalDateTime created_at;
	    private LocalDateTime updated_at;

	  
	}

	

