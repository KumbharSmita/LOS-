
 package com.imperacred.BankLoanApplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;
import com.imperacred.BankLoanApplication.model.LeadsStatusHistory;
import com.imperacred.BankLoanApplication.repository.LeadStatusHistoryRepository;
import com.imperacred.BankLoanApplication.service.LeadStatusHistoryService;

@Service 
  public class LeadStatusHistoryServiceImpl implements LeadStatusHistoryService {

	@Autowired
    private LeadStatusHistoryRepository leadStatusHistoryRepository;

	@Override
	public LeadStatusHistoryDTO saveLeadStatusHistory(LeadStatusHistoryDTO dto) {
		 LeadsStatusHistory entity = new LeadsStatusHistory(
		            dto.getHistory_id(),
		            dto.getLeads_id(),
		            dto.getAgent_id(),
		            dto.getStatus()
		           
		        );
 LeadsStatusHistory savedEntity = leadStatusHistoryRepository.save(entity);

		      
		        return new LeadStatusHistoryDTO(
		            savedEntity.getHistory_id(),
		            savedEntity.getLeadsId(),
		            savedEntity.getAgentId(),
		            savedEntity.getStatus(),
		            savedEntity.getUpdatedAt()
		        );
		    }

	@Override
	public List<LeadStatusHistoryDTO> getLeadStatusHistory(int leads_id) {
		 List<LeadsStatusHistory> historyList = leadStatusHistoryRepository.findByLeadsId(leads_id);

	        // Convert the list of entities to a list of DTOs
	        return historyList.stream()
	                .map(entity -> new LeadStatusHistoryDTO(
	                        entity.getHistory_id(),
	                        entity.getLeadsId(),
	                        entity.getAgentId(),
	                        entity.getStatus(),
	                        entity.getUpdatedAt()
	                ))
	                .collect(Collectors.toList());
	    }

	@Override
	public List<LeadStatusHistoryDTO> getAgentStatusHistory(int agent_id) {
		  List<LeadsStatusHistory> historyList = leadStatusHistoryRepository.findByAgentId(agent_id);

	        // Convert the list of entities to a list of DTOs
	        return historyList.stream()
	                .map(entity -> new LeadStatusHistoryDTO(
	                        entity.getHistory_id(),
	                        entity.getLeadsId(),
	                        entity.getAgentId(),
	                        entity.getStatus(),
	                        entity.getUpdatedAt()
	                ))
	                .collect(Collectors.toList());
	    }
	}
  
  