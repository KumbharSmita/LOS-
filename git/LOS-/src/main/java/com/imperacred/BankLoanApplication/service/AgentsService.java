package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.AgentLoginDTO;
import com.imperacred.BankLoanApplication.dto.AgentLoginResponseDTO;

public interface AgentsService {

	AgentLoginResponseDTO loginAgentAndGetToken(AgentLoginDTO dto);

	
}
