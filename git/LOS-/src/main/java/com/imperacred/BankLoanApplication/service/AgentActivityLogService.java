package com.imperacred.BankLoanApplication.service;

import java.util.List;

import com.imperacred.BankLoanApplication.dto.AgentActivityLogDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AgentActivityLogService {
	
	AgentActivityLogDTO logAgentActivity(AgentActivityLogDTO dto, HttpServletRequest request);
	
	 List<AgentActivityLogDTO> getLogByAgentId(Integer agent_id, HttpServletRequest request);

	
	List<AgentActivityLogDTO> getAllLogs(Integer agent_id, HttpServletRequest request);

	
}

