package com.imperacred.BankLoanApplication.dto;

import com.imperacred.BankLoanApplication.model.AgentRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentLoginDTO {
	private int agent_registration_id;
	private String email;
    private String password;
	
}
