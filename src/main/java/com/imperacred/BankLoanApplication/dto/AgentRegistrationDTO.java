package com.imperacred.BankLoanApplication.dto;

import com.imperacred.BankLoanApplication.model.AgentRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentRegistrationDTO {
	private int agent_registration_id;
	private String full_name;
    private String email;
    private String password;
    private String contactno;
    private String office_location;
    private String status;
	
	

}
