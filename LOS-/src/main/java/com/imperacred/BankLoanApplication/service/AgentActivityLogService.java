package com.imperacred.BankLoanApplication.service;

import java.util.List;

import com.imperacred.BankLoanApplication.dto.AgentActivityLogDTO;

public interface AgentActivityLogService {
	
	AgentActivityLogDTO logAgentActivity(AgentActivityLogDTO dto);
	
	 List<AgentActivityLogDTO> getLogByAgentId(int agent_id);
}
