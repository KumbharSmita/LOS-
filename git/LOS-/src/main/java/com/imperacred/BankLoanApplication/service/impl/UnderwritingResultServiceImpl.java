package com.imperacred.BankLoanApplication.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.config.AuthContext;
import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.Role;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
import com.imperacred.BankLoanApplication.repository.AgentsRepository;
import com.imperacred.BankLoanApplication.repository.LeadAssignmentRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.UnderwritingResultsRepository;
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UnderwritingResultServiceImpl implements UnderwritingResultsService {

    private static final Logger logger = LogManager.getLogger(UnderwritingResultServiceImpl.class);

    @Autowired
    private UnderwritingResultsRepository underwritingResultRepository;

    @Autowired
    private LeadsRepository leadRepository;

    @Autowired
    private LeadAssignmentRepository leadAssignmentRepository;

    @Autowired
    private AgentsRepository agentsRepository;

    @Autowired
    private AuthContext authContext;

    @Override
    public UnderwritingResultsDTO performUnderwriting(Integer leadsId, BigDecimal approvedAmount, HttpServletRequest request) {
        logger.info("Underwriting process started for Lead ID: {}", leadsId);

        if (approvedAmount == null || approvedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Approved amount must be greater than 0");
        }

        // --- Authorization check ---
        String userEmail = authContext.getLoggedInUserEmail(request);
        Role userRole = authContext.getLoggedInUserRole(request);

        Agents currentAgent = agentsRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in agent not found"));

        Integer currentAgentId = currentAgent.getAgent_id();

        if (userRole == Role.SUPER_ADMIN) {
            logger.info("User is SUPER_ADMIN, proceeding without assignment check.");
        } else if (userRole == Role.ADMIN) {
            
            boolean isAssigned = leadAssignmentRepository.existsByLeadsIdAndAgentId(leadsId, currentAgentId);
            if (!isAssigned) {
                logger.warn("Lead ID {} is not assigned to Admin agent ID {}", leadsId, currentAgentId);
                throw new RuntimeException("Access denied: Lead is not assigned to you.");
            }
        } else {
            logger.warn("User role {} not authorized to perform underwriting", userRole);
            throw new RuntimeException("Access denied: Unauthorized role.");
        }
        // --- Authorization check end ---

        // Check if underwriting already done
        List<UnderwritingResults> existingResults = underwritingResultRepository.findByLeadsId(leadsId);
        if (!existingResults.isEmpty()) {
            logger.warn("Underwriting already performed for Lead ID: {}", leadsId);
            throw new IllegalStateException("Underwriting already performed for this lead.");
        }

        Lead lead = leadRepository.findById(leadsId)
                .orElseThrow(() -> {
                    logger.error("Lead not found for ID: {}", leadsId);
                    return new RuntimeException("Lead not found");
                });

        Integer creditScore = lead.getCreditScore();
        logger.debug("Fetched credit score: {} for Lead ID: {}", creditScore, leadsId);

        String decision;
        String riskRating;
        String underwriterNotes;

        if (creditScore >= 800 && creditScore <= 900) {
            decision = "APPROVED";
            riskRating = "HIGH";
            underwriterNotes = "Loan approved with high creditworthiness.";
        } else if (creditScore >= 700 && creditScore < 800) {
            decision = "APPROVED";
            riskRating = "MEDIUM";
            underwriterNotes = "Loan approved with medium creditworthiness.";
        } else if (creditScore >= 500 && creditScore < 700) {
            decision = "CONDITIONAL";
            riskRating = "LOW";
            underwriterNotes = "Loan conditionally approved with low creditworthiness.";
        } else {
            decision = "REJECTED";
            riskRating = "LOW";
            underwriterNotes = "Credit score too low.";
            approvedAmount = BigDecimal.ZERO;
        }

        logger.info("Underwriting decision for Lead ID {}: {}, Risk: {}", leadsId, decision, riskRating);

        UnderwritingResults result = new UnderwritingResults();
        result.setLeadsId(leadsId);
        result.setRiskRating(riskRating);
        result.setApprovedAmount(approvedAmount);
        result.setDecision(decision);
        result.setUnderwriterNotes(underwriterNotes);
        result.setEvaluatedAt(LocalDateTime.now());

        underwritingResultRepository.save(result);
        logger.debug("Underwriting result saved for Lead ID: {}", leadsId);

        lead.setStatus(decision);
        leadRepository.save(lead);
        logger.info("Lead status updated to '{}' for Lead ID: {}", decision, leadsId);

        return new UnderwritingResultsDTO(
                leadsId,
                riskRating,
                approvedAmount,
                decision,
                underwriterNotes,
                result.getEvaluatedAt()
        );
    }

    @Override
    public UnderwritingResultsDTO getUnderwritingByLeadId(Integer leadsId) {
        UnderwritingResults result = underwritingResultRepository.findByLeadsId(leadsId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No underwriting result found for lead ID: " + leadsId));

        return new UnderwritingResultsDTO(
                result.getLeadsId(),
                result.getRiskRating(),
                result.getApprovedAmount(),
                result.getDecision(),
                result.getUnderwriterNotes(),
                result.getEvaluatedAt()
        );
    }

    @Override
    public List<UnderwritingResults> getAllUnderwritingResults() {
        return underwritingResultRepository.findAll();
    }
}
