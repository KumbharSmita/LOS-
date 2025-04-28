package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lead_status_history") 
public class LeadsStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int history_id;

    @Column(name = "leads_id")
    private int leadsId;

    @Column(name = "agent_id")
    private int agentId;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

	public LeadsStatusHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeadsStatusHistory(int history_id, int leadsId, int agentId, String status) {
		super();
		this.history_id = history_id;
		this.leadsId = leadsId;
		this.agentId = agentId;
		this.status = status;
		this.updatedAt = LocalDateTime.now();
	}

	public int getHistory_id() {
		return history_id;
	}

	public void setHistory_id(int history_id) {
		this.history_id = history_id;
	}

	public int getLeadsId() {
		return leadsId;
	}

	public void setLeadsId(int leadsId) {
		this.leadsId = leadsId;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

    
}
