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

		public AgentLoads() {
			super();
			// TODO Auto-generated constructor stub
		}

		public AgentLoads(int load_id, int agent_id, int leadCount, LocalDateTime lastAssigned) {
			super();
			this.load_id = load_id;
			this.agent_id = agent_id;
			this.leadCount = leadCount;
			this.lastAssigned = lastAssigned;
		}

		public int getLoad_id() {
			return load_id;
		}

		public void setLoad_id(int load_id) {
			this.load_id = load_id;
		}

		public int getAgent_id() {
			return agent_id;
		}

		public void setAgent_id(int agent_id) {
			this.agent_id = agent_id;
		}

		public int getLeadCount() {
			return leadCount;
		}

		public void setLeadCount(int leadCount) {
			this.leadCount = leadCount;
		}

		public LocalDateTime getLastAssigned() {
			return lastAssigned;
		}

		public void setLastAssigned(LocalDateTime lastAssigned) {
			this.lastAssigned = lastAssigned;
		}

		
	}

	
	
	

