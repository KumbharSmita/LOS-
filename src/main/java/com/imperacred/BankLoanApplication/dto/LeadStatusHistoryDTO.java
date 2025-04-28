package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

public class LeadStatusHistoryDTO {

    private int history_id;  
    private int leads_id;   
    private int agent_id;    
    private String status;
    private LocalDateTime updated_at;
	public LeadStatusHistoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LeadStatusHistoryDTO(int history_id, int leads_id, int agent_id, String status, LocalDateTime updated_at) {
		super();
		this.history_id = history_id;
		this.leads_id = leads_id;
		this.agent_id = agent_id;
		this.status = status;
		this.updated_at = updated_at;
	}
	public int getHistory_id() {
		return history_id;
	}
	public void setHistory_id(int history_id) {
		this.history_id = history_id;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}  

   
}
