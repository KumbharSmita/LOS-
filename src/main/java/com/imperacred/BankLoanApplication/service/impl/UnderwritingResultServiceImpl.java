package com.imperacred.BankLoanApplication.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.UnderwritingResultsRepository;
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

@Service
public class UnderwritingResultServiceImpl implements UnderwritingResultsService {

    private static final Logger logger = LogManager.getLogger(UnderwritingResultServiceImpl.class);

    @Autowired
    private UnderwritingResultsRepository underwritingResultRepository;

    @Autowired
    private LeadsRepository leadRepository;

    @Override
    public UnderwritingResultsDTO performUnderwriting(Integer leadsId) {
        logger.info("Underwriting process started for Lead ID: {}", leadsId);
        
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

        double requestedAmount = lead.getAmount();

        String decision;
        String riskRating;
        String underwriterNotes;
        BigDecimal approvedAmount;

        if (creditScore >= 800 && creditScore <= 900) {
            decision = "APPROVED";
            riskRating = "HIGH";
            underwriterNotes = "Loan approved with high creditworthiness.";
            approvedAmount = BigDecimal.valueOf(requestedAmount);
        } else if (creditScore >= 700 && creditScore < 800) {
            decision = "APPROVED";
            riskRating = "MEDIUM";
            underwriterNotes = "Loan approved with medium creditworthiness.";
            approvedAmount = BigDecimal.valueOf(requestedAmount);
        } else if (creditScore >= 500 && creditScore < 700) {
            decision = "CONDITIONAL";
            riskRating = "LOW";
            underwriterNotes = "Loan conditionally approved with low creditworthiness.";
            approvedAmount = BigDecimal.valueOf(requestedAmount);
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
}
