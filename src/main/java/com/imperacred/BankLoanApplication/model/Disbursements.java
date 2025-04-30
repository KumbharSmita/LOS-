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
@Table(name="disbursements")
public class Disbursements {

	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "disbursements_id") 
	    private int disbursementsId;

	    @Column(name = "application_id") 
	    private int applicationId;

	    @Column(name = "disbursed_amount") 
	    private BigDecimal disbursedAmount;

	    @Column(name = "processing_fee") 
	    private BigDecimal processingFee;

	    @Column(name = "actual_amount_credited") 
	    private BigDecimal actualAmountCredited;

	    @Column(name = "bank_account") 
	    private String bankAccount;

	    @Column(name = "utr_number") 
	    private String utrNumber;

	    @Column(name = "disbursed_at") 
	    private LocalDateTime disbursedAt;

	    @Column(name = "status") 
	    private String status;

	   
	}


