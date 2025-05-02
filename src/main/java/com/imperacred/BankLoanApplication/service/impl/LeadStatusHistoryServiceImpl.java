package com.imperacred.BankLoanApplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;
import com.imperacred.BankLoanApplication.model.LeadsStatusHistory;
import com.imperacred.BankLoanApplication.repository.LeadStatusHistoryRepository;
import com.imperacred.BankLoanApplication.service.LeadStatusHistoryService;

@Service
public class LeadStatusHistoryServiceImpl implements LeadStatusHistoryService {

    private static final Logger logger = LogManager.getLogger(LeadStatusHistoryServiceImpl.class);

    @Autowired
    private LeadStatusHistoryRepository leadStatusHistoryRepository;

    @Override
    public LeadStatusHistoryDTO saveLeadStatusHistory(LeadStatusHistoryDTO dto) {
        logger.info("Saving lead status history for Lead ID: {}, Agent ID: {}, Status: {}",
                dto.getLeads_id(), dto.getAgent_id(), dto.getStatus());

        LeadsStatusHistory entity = new LeadsStatusHistory(
                dto.getHistory_id(),
                dto.getLeads_id(),
                dto.getAgent_id(),
                dto.getStatus()
        );

        LeadsStatusHistory savedEntity = leadStatusHistoryRepository.save(entity);

        logger.debug("Saved entity: {}", savedEntity);

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
        logger.info("Fetching status history for Lead ID: {}", leads_id);

        List<LeadsStatusHistory> historyList = leadStatusHistoryRepository.findByLeadsId(leads_id);

        if (historyList.isEmpty()) {
            logger.warn("No status history found for Lead ID: {}", leads_id);
        } else {
            logger.debug("Found {} status history entries for Lead ID: {}", historyList.size(), leads_id);
        }

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
        logger.info("Fetching status history for Agent ID: {}", agent_id);

        List<LeadsStatusHistory> historyList = leadStatusHistoryRepository.findByAgentId(agent_id);

        if (historyList.isEmpty()) {
            logger.warn("No status history found for Agent ID: {}", agent_id);
        } else {
            logger.debug("Found {} status history entries for Agent ID: {}", historyList.size(), agent_id);
        }

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
