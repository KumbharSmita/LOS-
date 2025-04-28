package com.imperacred.BankLoanApplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LeadAssignmentRequestDTO {

    @JsonProperty("leads_id")
    private int leadsId;

	public LeadAssignmentRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeadAssignmentRequestDTO(int leadsId) {
		super();
		this.leadsId = leadsId;
	}

	public int getLeadsId() {
		return leadsId;
	}

	public void setLeadsId(int leadsId) {
		this.leadsId = leadsId;
	}

}
