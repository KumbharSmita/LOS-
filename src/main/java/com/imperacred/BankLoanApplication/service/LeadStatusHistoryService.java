
  package com.imperacred.BankLoanApplication.service;

import java.util.List;

import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;

public interface LeadStatusHistoryService { 
	 
	  LeadStatusHistoryDTO saveLeadStatusHistory(LeadStatusHistoryDTO dto);
	 
	  List<LeadStatusHistoryDTO> getLeadStatusHistory(int leads_id);
	  List<LeadStatusHistoryDTO> getAgentStatusHistory(int agent_id);
 }
 
