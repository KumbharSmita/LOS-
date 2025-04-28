package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LeadAssignmentResponseDTO {

	
    private int leads_id;
	private int agent_id;
	private LocalDateTime  assigned_at ;
	private String status;
	public LeadAssignmentResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LeadAssignmentResponseDTO(int leads_id, int agent_id, LocalDateTime assigned_at, String status) {
		super();
		this.leads_id = leads_id;
		this.agent_id = agent_id;
		this.assigned_at = assigned_at;
		this.status = status;
	}
	public int getLeads_id() {
		return leads_id;
	}
	public void setLeads_id(int leads_id) {
		this.leads_id = leads_id;
	}
	public int getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(int agent_id) {
		this.agent_id = agent_id;
	}
	public LocalDateTime getAssigned_at() {
		return assigned_at;
	}
	public void setAssigned_at(LocalDateTime assigned_at) {
		this.assigned_at = assigned_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
