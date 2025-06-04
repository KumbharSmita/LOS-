package com.imperacred.BankLoanApplication.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.exception.LeadAssignmentException;
import com.imperacred.BankLoanApplication.model.AgentLoads;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.LeadAssignments;
import com.imperacred.BankLoanApplication.model.Role;
import com.imperacred.BankLoanApplication.repository.AgentLoadsRepository;
import com.imperacred.BankLoanApplication.repository.LeadAssignmentRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;

import jakarta.transaction.Transactional;

@Service
public class LeadAssignmentServiceImpl implements LeadAssignmentService {

    private static final Logger logger = LogManager.getLogger(LeadAssignmentServiceImpl.class);

    @Autowired
    private LeadsRepository leadsRepo;

    private final AgentLoadsRepository agentLoadsRepo;
    private final LeadAssignmentRepository leadAssignmentRepo;

    @Autowired
    public LeadAssignmentServiceImpl(AgentLoadsRepository agentLoadsRepo, LeadAssignmentRepository leadAssignmentRepo) {
        this.agentLoadsRepo = agentLoadsRepo;
        this.leadAssignmentRepo = leadAssignmentRepo;
    }

    @Transactional
    @Override
    public LeadAssignmentResponseDTO assignLeadToAgent(Integer leads_id) {
        logger.info("Starting lead assignment for lead ID: {}", leads_id);

        boolean alreadyAssigned = leadAssignmentRepo.existsByLeadsIdAndStatus(leads_id, "ASSIGNED");
        if (alreadyAssigned) {
            logger.info("Lead ID {} is already assigned. Skipping reassignment.", leads_id);
            throw new LeadAssignmentException("Lead is already assigned to an agent.");
        }

        Lead lead = leadsRepo.findById(leads_id)
                .orElseThrow(() -> {
                    logger.error("Lead not found with ID: {}", leads_id);
                    return new LeadAssignmentException("Lead not found");
                });

        if (lead.getCreditScore() == null || lead.getCreditScore() < 700) {
            logger.warn("Lead ID {} has insufficient credit score: {}", leads_id, lead.getCreditScore());
            throw new LeadAssignmentException("Credit score is too low to assign this lead to an agent.");
        }

        // Fetch all agents sorted by lead count and last assigned (lowest load first)
        List<AgentLoads> agentLoadsList = agentLoadsRepo.findAllByOrderByLeadCountAscLastAssignedAsc();

        Agents assignedAgent = null;
        AgentLoads assignedAgentLoad = null;

        for (AgentLoads agentLoad : agentLoadsList) {
            Agents agent = agentLoad.getAgent();

            if (agent.getRole() == Role.SUPER_ADMIN) {
                logger.warn("Skipping agent ID {} with role SUPER_ADMIN for lead assignment.", agent.getAgent_id());
                continue;  // Skip SUPER_ADMIN agents
            }

            if (agent.getRole() == Role.ADMIN) {
                assignedAgent = agent;
                assignedAgentLoad = agentLoad;
                break;  // Found suitable agent, stop searching
            }
        }

        if (assignedAgent == null) {
            logger.error("No ADMIN agent available for lead assignment.");
            throw new LeadAssignmentException("No ADMIN agent available for assignment.");
        }

        // Proceed with assignment to the found admin agent
        assignedAgentLoad.setLeadCount(assignedAgentLoad.getLeadCount() + 1);
        assignedAgentLoad.setLastAssigned(LocalDateTime.now());
        agentLoadsRepo.save(assignedAgentLoad);

        LeadAssignments assignment = new LeadAssignments();
        assignment.setLeadsId(leads_id);
        assignment.setAgentId(assignedAgent.getAgent_id());
        assignment.setAssigned_at(LocalDateTime.now());
        assignment.setStatus("ASSIGNED");
        leadAssignmentRepo.save(assignment);

        logger.info("Lead ID {} ({} {}) assigned to Agent ID {} at {}", 
            leads_id, 
            lead.getFirstName(), 
            lead.getLastName(), 
            assignedAgent.getAgent_id(), 
            assignment.getAssigned_at());

        LeadsDTO leadDTO = new LeadsDTO(
            lead.getLeadsId(),
            lead.getFirstName(),
            lead.getLastName(),
            lead.getEmail(),
            lead.getPhone(),
            lead.getPanNumber(),
            lead.getAadhaarNumber(),
            lead.getSource(),
            lead.getLoanType(),
            lead.getAmount(),
            lead.getTenureMonths(),
            lead.getPurpose()
        );

        return new LeadAssignmentResponseDTO(
            assignment.getLead_assignment_id(),
            leadDTO,
            assignedAgent.getAgent_id(),
            assignment.getAssigned_at(),
            assignment.getStatus()
        );
    }

    @Override
    public List<LeadAssignmentResponseDTO> getAssignedLeadsForAgent(Integer agentId) {
        logger.info("Fetching assigned leads for agent ID: {}", agentId);

        List<LeadAssignments> assignments = leadAssignmentRepo.findByAgentId(agentId);

        return assignments.stream()
            .map(assignment -> {
                Optional<Lead> optionalLead = leadsRepo.findById(assignment.getLeadsId());
                if (optionalLead.isEmpty()) {
                    logger.warn("Lead not found for ID: {}", assignment.getLeadsId());
                    return null;
                }

                Lead lead = optionalLead.get();

                LeadsDTO leadDTO = new LeadsDTO(
                		lead.getLeadsId(),
                        lead.getFirstName(),
                        lead.getLastName(),
                        lead.getEmail(),
                        lead.getPhone(),
                        lead.getPanNumber(),
                        lead.getAadhaarNumber(),
                        lead.getSource(),
                        lead.getLoanType(),
                        lead.getAmount(),
                        lead.getTenureMonths(),
                        lead.getPurpose()
                );

                return new LeadAssignmentResponseDTO(
                        assignment.getLead_assignment_id(),
                        leadDTO,
                        assignment.getAgentId(),
                        assignment.getAssigned_at(),
                        assignment.getStatus()
                );
            })
            .filter(dto -> dto != null)
            .toList();
    }
    
    @Override
    public List<LeadAssignmentResponseDTO> getAssignedLeadsForAgentByStatus(Integer agentId, String status) {
        logger.info("Filtering assigned leads for agent {} by status: {}", agentId, status);

        return leadAssignmentRepo.findByAgentId(agentId).stream()
            .map(assignment -> {
                Optional<Lead> leadOpt = leadsRepo.findById(assignment.getLeadsId());
                if (leadOpt.isEmpty()) return null;
                Lead lead = leadOpt.get();
                if (!lead.getStatus().equalsIgnoreCase(status)) return null;

                LeadsDTO leadDTO = new LeadsDTO(
                    lead.getLeadsId(),
                    lead.getFirstName(),
                    lead.getLastName(),
                    lead.getEmail(),
                    lead.getPhone(),
                    lead.getPanNumber(),
                    lead.getAadhaarNumber(),
                    lead.getSource(),
                    lead.getLoanType(),
                    lead.getAmount(),
                    lead.getTenureMonths(),
                    lead.getPurpose()
                );

                return new LeadAssignmentResponseDTO(
                    assignment.getLead_assignment_id(),
                    leadDTO,
                    assignment.getAgentId(),
                    assignment.getAssigned_at(),
                    assignment.getStatus() 
                );
            })
            .filter(dto -> dto != null)
            .toList();
    }
    @Override
    public LeadAssignmentResponseDTO getAssignmentByLeadId(Integer leadId) {
        logger.info("Fetching assignment info for lead ID: {}", leadId);

        Optional<LeadAssignments> assignmentOpt = leadAssignmentRepo.findByLeadsIdAndStatus(leadId, "ASSIGNED");

        if (assignmentOpt.isEmpty()) {
            logger.info("No assignment found for lead ID: {}", leadId);
            return null;  // or throw a custom exception if preferred
        }

        LeadAssignments assignment = assignmentOpt.get();

        Optional<Lead> leadOpt = leadsRepo.findById(leadId);
        if (leadOpt.isEmpty()) {
            logger.warn("Lead not found for ID: {}", leadId);
            return null;
        }

        Lead lead = leadOpt.get();

        LeadsDTO leadDTO = new LeadsDTO(
            lead.getLeadsId(),
            lead.getFirstName(),
            lead.getLastName(),
            lead.getEmail(),
            lead.getPhone(),
            lead.getPanNumber(),
            lead.getAadhaarNumber(),
            lead.getSource(),
            lead.getLoanType(),
            lead.getAmount(),
            lead.getTenureMonths(),
            lead.getPurpose()
        );

        return new LeadAssignmentResponseDTO(
            assignment.getLead_assignment_id(),
            leadDTO,
            assignment.getAgentId(),
            assignment.getAssigned_at(),
            assignment.getStatus()
        );
    }



    
}
