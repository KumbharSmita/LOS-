package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.config.AuthContext;
import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;
import com.imperacred.BankLoanApplication.exception.UnauthorizedAccessException;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.model.LeadsStatusHistory;
import com.imperacred.BankLoanApplication.model.Role;
import com.imperacred.BankLoanApplication.repository.AgentsRepository;
import com.imperacred.BankLoanApplication.repository.LeadStatusHistoryRepository;
import com.imperacred.BankLoanApplication.service.LeadStatusHistoryService;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeadStatusHistoryServiceImpl implements LeadStatusHistoryService {

	private static final Logger logger = LogManager.getLogger(LeadStatusHistoryServiceImpl.class);

	@Autowired
	private LeadStatusHistoryRepository leadStatusHistoryRepository;

	@Autowired
	private AgentsRepository agentsRepository;

	@Autowired
	private AuthContext authContext;

	@Override
	public LeadStatusHistoryDTO saveLeadStatusHistory(LeadStatusHistoryDTO dto, HttpServletRequest request) {
		logger.info("Entering saveLeadStatusHistory with DTO: {}", dto);

		// Fetch the logged-in user's email
		String loggedInAgentEmail = authContext.getLoggedInUserEmail(request);
		if (loggedInAgentEmail == null) {
			logger.error("User is not authenticated.");
			throw new UnauthorizedAccessException("User is not authenticated.");
		}

		// Get the logged-in agent by email
		Agents loggedInAgent = agentsRepository.findByEmail(loggedInAgentEmail).orElseThrow(() -> {
			logger.error("Agent with email {} not found.", loggedInAgentEmail);
			return new UnauthorizedAccessException("Agent not found");
		});

		// Check if the logged-in agent has the required permissions to save the status
		// history
		if (!Role.SUPER_ADMIN.equals(loggedInAgent.getRole())
				&& !loggedInAgent.getAgent_id().equals(dto.getAgent_id())) {
			logger.error("Agent {} does not have permission to save history for agent {}", loggedInAgent.getAgent_id(),
					dto.getAgent_id());
			throw new UnauthorizedAccessException("You do not have permission to perform this action.");
		}

		// Create the entity from the DTO
		LeadsStatusHistory entity = new LeadsStatusHistory();
		entity.setLeadsId(dto.getLeads_id());
		entity.setAgentId(dto.getAgent_id());
		entity.setStatus(dto.getStatus());
		entity.setUpdatedAt(dto.getUpdated_at() != null ? dto.getUpdated_at() : java.time.LocalDateTime.now());

		// Save the entity in the database
		LeadsStatusHistory savedEntity = leadStatusHistoryRepository.save(entity);

		logger.info("Successfully saved lead status history: {}", savedEntity);

		// Return a DTO with the saved entity details
		return new LeadStatusHistoryDTO(savedEntity.getHistory_id(), savedEntity.getLeadsId(), savedEntity.getAgentId(),
				savedEntity.getStatus(), savedEntity.getUpdatedAt());
	}
	@Override
	public List<LeadStatusHistoryDTO> getLeadStatusHistory(Integer leads_id, HttpServletRequest request) {
	    logger.info("Entering getLeadStatusHistory for leads_id: {}", leads_id);

	    // Fetch the logged-in user's email
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

	    // Fetch the lead status history for the given lead ID
	    List<LeadsStatusHistory> historyList = leadStatusHistoryRepository.findByLeadsId(leads_id);

	    // Debug: Show fetched history
	    logger.info("Fetched {} history entries for lead {}", historyList.size(), leads_id);

	    // Permission enforcement for non-super_admin
	    if (!Role.SUPER_ADMIN.equals(loggedInAgent.getRole())) {
	        // If it's not a super_admin, filter the history list to show only entries related to this agent's leads
	        if (!Objects.equals(loggedInAgent.getAgent_id(), historyList.get(0).getAgentId())) {
	            logger.error("Agent {} does not have permission to access lead {}'s history.", loggedInAgent.getAgent_id(), leads_id);
	            throw new UnauthorizedAccessException("You do not have permission to access this lead's history.");
	        }
	    }

	    // Return the history entries as DTOs
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
	public List<LeadStatusHistoryDTO> getAgentStatusHistory(Integer agent_id, HttpServletRequest request) {
	    logger.info("Entering getAgentStatusHistory for agent_id: {}", agent_id);

	    // Fetch the logged-in user's email
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
	        // Admins cannot access other agents' histories, only their own
	        if (!Objects.equals(loggedInAgent.getAgent_id(), agent_id)) {
	            logger.error("Admin agent {} does not have permission to access history for agent {}", loggedInAgent.getAgent_id(), agent_id);
	            throw new UnauthorizedAccessException("You do not have permission to access this agent's history.");
	        }
	    }

	    // Fetch the lead status history for the given agent ID
	    List<LeadsStatusHistory> historyList = leadStatusHistoryRepository.findByAgentId(agent_id);

	    // Return the history entries as DTOs
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