package com.imperacred.BankLoanApplication.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.AgentActivityLogDTO;
import com.imperacred.BankLoanApplication.model.AgentActivityLog;
import com.imperacred.BankLoanApplication.repository.AgentActivityLogRepository;
import com.imperacred.BankLoanApplication.service.AgentActivityLogService;

@Service
public class AgentActivityLogServiceImpl implements AgentActivityLogService {
  
    @Autowired
    private AgentActivityLogRepository agentActivityLogRepository;

    @Override
    public AgentActivityLogDTO logAgentActivity(AgentActivityLogDTO dto) {
       
        AgentActivityLog log = new AgentActivityLog();
        log.setAgentId(dto.getAgent_id());
        log.setActionType(dto.getActionType());
        log.setActionDetails(dto.getActionDetails());
        log.setActionTimestamp(LocalDateTime.now());  
        
      
        AgentActivityLog savedLog = agentActivityLogRepository.save(log);

        
        AgentActivityLogDTO savedDto = new AgentActivityLogDTO();
        savedDto.setLog_id(savedLog.getLog_id()); 
        savedDto.setAgent_id(savedLog.getAgentId());
        savedDto.setActionType(savedLog.getActionType());
        savedDto.setActionDetails(savedLog.getActionDetails());
        savedDto.setActionTimestamp(savedLog.getActionTimestamp());

        return savedDto;
    }

    @Override
    public List<AgentActivityLogDTO> getLogByAgentId(int agent_id) {
        
        List<AgentActivityLog> logs = agentActivityLogRepository.findByAgentId(agent_id);

        
        return logs.stream()
                   .map(this::convertToDTO)
                   .collect(Collectors.toList());
    }

   
    private AgentActivityLogDTO convertToDTO(AgentActivityLog log) {
        AgentActivityLogDTO dto = new AgentActivityLogDTO();
        dto.setLog_id(log.getLog_id());  
        dto.setAgent_id(log.getAgentId());  
        dto.setActionType(log.getActionType());
        dto.setActionDetails(log.getActionDetails());
        dto.setActionTimestamp(log.getActionTimestamp());
        return dto;
    }
}
