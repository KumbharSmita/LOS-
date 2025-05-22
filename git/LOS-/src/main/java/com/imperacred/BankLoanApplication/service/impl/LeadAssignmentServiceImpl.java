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
import com.imperacred.BankLoanApplication.model.AgentLoads;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.LeadAssignments;
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

        Lead lead = leadsRepo.findById(leads_id)
                .orElseThrow(() -> {
                    logger.error("Lead not found with ID: {}", leads_id);
                    return new RuntimeException("Lead not found");
                });

        AgentLoads agentLoads = agentLoadsRepo.findFirstByOrderByLeadCountAscLastAssignedAsc()
                .orElseThrow(() -> {
                    logger.error("No available agents for lead assignment");
                    return new RuntimeException("No agents available");
                });

        agentLoads.setLeadCount(agentLoads.getLeadCount() + 1);
        agentLoads.setLastAssigned(LocalDateTime.now());
        agentLoadsRepo.save(agentLoads);

        LeadAssignments assignment = new LeadAssignments();
        assignment.setLeads_id(leads_id);
        assignment.setAgentId(agentLoads.getAgent_id());
        assignment.setAssigned_at(LocalDateTime.now());
        assignment.setStatus("ASSIGNED");
        leadAssignmentRepo.save(assignment);

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
                agentLoads.getAgent_id(),
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
                Optional<Lead> optionalLead = leadsRepo.findById(assignment.getLeads_id());
                if (optionalLead.isEmpty()) {
                    logger.warn("Lead not found for ID: {}", assignment.getLeads_id());
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
                Optional<Lead> leadOpt = leadsRepo.findById(assignment.getLeads_id());
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

    
}
