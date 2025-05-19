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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
	@Table(name = "agent_loads")
	
	public class AgentLoads {
	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private int load_id;
	    
	    private int agent_id;


	    @Column(name = "lead_count")  
	    private int leadCount;   
	     
	    @Column(name = "last_assigned")
	    private LocalDateTime lastAssigned;
		
	}

	
	
	

