package com.imperacred.BankLoanApplication.service;

import java.util.List;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.model.Lead;

public interface LeadAssignmentService {
	    LeadAssignmentResponseDTO assignLeadToAgent(Integer  leads_id);
	    List<LeadAssignmentResponseDTO> getAssignedLeadsForAgent(Integer agentId);
	    List<LeadAssignmentResponseDTO> getAssignedLeadsForAgentByStatus(Integer agentId, String status);
		LeadAssignmentResponseDTO getAssignmentByLeadId(Integer leadId);

	}

	  
	



