package com.imperacred.BankLoanApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentDTO {

	private int agent_id;
	private int agent_registration_id;
	private String full_name;
	private String email;
	private String contactno;
	private String office_location;
	private String status;
	
}
