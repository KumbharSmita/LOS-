package com.imperacred.BankLoanApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.AgentActivityLog;

public interface AgentActivityLogRepository extends JpaRepository<AgentActivityLog,Integer>{

	List<AgentActivityLog> findByAgentId(Integer agent_id);
	
}
