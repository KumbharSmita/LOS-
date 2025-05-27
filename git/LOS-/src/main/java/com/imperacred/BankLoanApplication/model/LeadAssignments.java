package com.imperacred.BankLoanApplication.model;

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

@Entity
	@Table(name = "lead_assignment")
	@Data 
	@NoArgsConstructor 
	@AllArgsConstructor
	public class LeadAssignments {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int lead_assignment_id;

	    @Column(name = "leads_id")
	    private Integer leadsId;  
	    @Column(name="agent_id")
	    private Integer agentId;
	    private LocalDateTime assigned_at;
	    private String status;
		
	}

	

