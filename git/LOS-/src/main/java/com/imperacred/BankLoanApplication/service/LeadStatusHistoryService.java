package com.imperacred.BankLoanApplication.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;

public interface LeadStatusHistoryService {

    LeadStatusHistoryDTO saveLeadStatusHistory(LeadStatusHistoryDTO dto, HttpServletRequest request);

    List<LeadStatusHistoryDTO> getLeadStatusHistory(Integer leads_id, HttpServletRequest request);

    List<LeadStatusHistoryDTO> getAgentStatusHistory(Integer agent_id, HttpServletRequest request);
}
