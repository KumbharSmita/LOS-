package com.imperacred.BankLoanApplication.service;

import java.util.List;

import com.imperacred.BankLoanApplication.dto.AgentLoginDTO;
import com.imperacred.BankLoanApplication.dto.AgentLoginResponseDTO;
import com.imperacred.BankLoanApplication.dto.AgentRegistrationDTO;
import com.imperacred.BankLoanApplication.model.AgentRegistration;
import com.imperacred.BankLoanApplication.model.Agents;

public interface AgentRegistrationLoginService {

	AgentRegistration registerAgent(AgentRegistrationDTO dto);
	
	
	
	AgentLoginResponseDTO loginAgentAndGetToken(AgentLoginDTO dto);
    
    Agents getAgentById(int agents_id);

    List<Agents> getAllAgents();

	Agents updateAgent(int agents_id, AgentRegistrationDTO agentDTO);

	String deleteAgent(int agents_id);

	
}





