
  package com.imperacred.BankLoanApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.LeadsStatusHistory;

public interface LeadStatusHistoryRepository extends JpaRepository<LeadsStatusHistory, Integer> {

	List<LeadsStatusHistory> findByLeadsId(int leads_id);

	List<LeadsStatusHistory> findByAgentId(int agent_id);
 
  }