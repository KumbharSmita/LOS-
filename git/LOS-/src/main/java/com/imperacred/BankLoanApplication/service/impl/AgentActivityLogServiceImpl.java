package com.imperacred.BankLoanApplication.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.config.AuthContext;
import com.imperacred.BankLoanApplication.dto.AgentActivityLogDTO;
import com.imperacred.BankLoanApplication.exception.ResourceNotFoundException;
import com.imperacred.BankLoanApplication.exception.UnauthorizedAccessException;
import com.imperacred.BankLoanApplication.model.AgentActivityLog;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.model.Role;
import com.imperacred.BankLoanApplication.repository.AgentActivityLogRepository;
import com.imperacred.BankLoanApplication.repository.AgentsRepository;
import com.imperacred.BankLoanApplication.service.AgentActivityLogService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AgentActivityLogServiceImpl implements AgentActivityLogService {

    private static final Logger logger = LogManager.getLogger(AgentActivityLogServiceImpl.class);

    @Autowired
    private AgentActivityLogRepository agentActivityLogRepository;
    @Autowired
    private AuthContext authContext;
    @Autowired
    private AgentsRepository agentsRepository;
    @Transactional
    @Override
    public AgentActivityLogDTO logAgentActivity(AgentActivityLogDTO dto, HttpServletRequest request) {
        logger.info("Logging activity for Agent ID: {}", dto.getAgent_id());

        String loggedInEmail = authContext.getLoggedInUserEmail(request);
        if (loggedInEmail == null) {
            logger.error("User not authenticated");
            throw new UnauthorizedAccessException("User is not authenticated.");
        }

        Agents loggedInAgent = agentsRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new UnauthorizedAccessException("Agent not found."));

        logger.info("Logged-in user's role: {}", loggedInAgent.getRole());

        // Restrict ADMIN to log only their own activity
        if (Role.ADMIN.equals(loggedInAgent.getRole())) {
            if (!dto.getAgent_id().equals(loggedInAgent.getAgent_id())) {
                logger.error("ADMIN agent {} attempted to log activity for agent {}", 
                    loggedInAgent.getAgent_id(), dto.getAgent_id());
                throw new UnauthorizedAccessException("ADMINs can only log their own activity.");
            }
        }

        // SUPER_ADMIN can log for anyone, no restriction
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

        logger.info("Activity log saved for Agent ID: {} with Log ID: {}", dto.getAgent_id(), savedLog.getLog_id());

        return savedDto;
    }


    @Override
    public List<AgentActivityLogDTO> getLogByAgentId(Integer agent_id, HttpServletRequest request) {
        logger.info("Fetching activity logs for Agent ID: {}", agent_id);

        // Fetch the logged-in user's role from the request
        String loggedInAgentEmail = authContext.getLoggedInUserEmail(request);
        if (loggedInAgentEmail == null) {
            logger.error("User is not authenticated.");
            throw new UnauthorizedAccessException("User is not authenticated.");
        }

        // Get the logged-in agent by email
        Agents loggedInAgent = agentsRepository.findByEmail(loggedInAgentEmail)
                .orElseThrow(() -> {
                    logger.error("Agent with email {} not found.", loggedInAgentEmail);
                    return new UnauthorizedAccessException("Agent not found");
                });

        // Permission enforcement for non-super_admin
        if (!Role.SUPER_ADMIN.equals(loggedInAgent.getRole())) {
            // Admins can only access their own logs
            if (loggedInAgent.getAgent_id() != agent_id) {
                logger.error("Admin agent {} does not have permission to access history for agent {}", loggedInAgent.getAgent_id(), agent_id);
                throw new UnauthorizedAccessException("You do not have permission to access this agent's history.");
            }
        }

        // Fetch the activity logs based on the agent_id
        List<AgentActivityLog> logs = agentActivityLogRepository.findByAgentId(agent_id);

        if (logs.isEmpty()) {
            logger.warn("No activity logs found for Agent ID: {}", agent_id);
            throw new ResourceNotFoundException("No activity logs found for this agent.");
        }

        logger.info("Found {} activity logs for Agent ID: {}", logs.size(), agent_id);

        // Convert logs to DTOs and return
        return logs.stream()
                .map(this::convertToDTO)  // Use convertToDTO method here
                .collect(Collectors.toList());
    }

    // Convert AgentActivityLog entity to AgentActivityLogDTO
    private AgentActivityLogDTO convertToDTO(AgentActivityLog log) {
        AgentActivityLogDTO dto = new AgentActivityLogDTO();
        dto.setLog_id(log.getLog_id());
        dto.setAgent_id(log.getAgentId());
        dto.setActionType(log.getActionType());
        dto.setActionDetails(log.getActionDetails());
        dto.setActionTimestamp(log.getActionTimestamp());
        return dto;
    }
  
    @Override
    public List<AgentActivityLogDTO> getAllLogs(Integer agent_id, HttpServletRequest request) {
        String loggedInEmail = authContext.getLoggedInUserEmail(request);
        if (loggedInEmail == null) {
            throw new UnauthorizedAccessException("User not authenticated.");
        }

        // Fetch logged-in agent from the database based on email
        Agents loggedInAgent = agentsRepository.findByEmail(loggedInEmail)
            .orElseThrow(() -> new UnauthorizedAccessException("Agent not found."));

        // Log the role of the logged-in agent
        logger.info("Logged-in user's role: {}", loggedInAgent.getRole());

        // Check if the logged-in user is a SUPER_ADMIN
        if (!Role.SUPER_ADMIN.equals(loggedInAgent.getRole())) {
            // Log the role and deny access to non-SUPER_ADMINs
            logger.error("Access denied for user with role: {}", loggedInAgent.getRole());
            throw new UnauthorizedAccessException("Access denied. Only SUPER_ADMIN can view all logs.");
        }

        List<AgentActivityLog> logs;
        if (agent_id != null) {
            // Fetch activity logs filtered by agent_id
            logs = agentActivityLogRepository.findByAgentId(agent_id);
        } else {
            // If no agent_id is provided, return all logs for SUPER_ADMIN
            logs = agentActivityLogRepository.findAll();
        }

        return logs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


}
