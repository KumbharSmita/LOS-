package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.BorrowerSelectionDTO;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.UnderwritingResultsRepository;
import com.imperacred.BankLoanApplication.service.BorrowerSelectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BorrowerSelectionServiceImpl implements BorrowerSelectionService {

    private static final Logger logger = LogManager.getLogger(BorrowerSelectionServiceImpl.class);
  


    @Autowired
    private LeadsRepository leadRepository;
    @Autowired
    private UnderwritingResultsRepository underwritingResultsRepository;

    @Override
    public BorrowerSelectionDTO confirmLoanSelection(Integer leadsId, BorrowerSelectionDTO selectionDTO) {
        logger.info("Processing loan confirmation for Lead ID: {}", leadsId);

        Lead lead = leadRepository.findById(leadsId)
                .orElseThrow(() -> {
                    logger.error("Lead not found with ID: {}", leadsId);
                    return new RuntimeException("Lead not found with ID: " + leadsId);
                });

        if ("Confirmed by Borrower".equalsIgnoreCase(lead.getStatus())) {
            logger.warn("Duplicate confirmation attempt for Lead ID: {}", leadsId);
            throw new IllegalStateException("Loan selection has already been confirmed by the borrower.");
        }

        // Fetch latest underwriting result for this lead
        UnderwritingResults latestResult = underwritingResultsRepository.findByLeadsId(leadsId).stream()
            .max((r1, r2) -> r1.getEvaluatedAt().compareTo(r2.getEvaluatedAt()))
            .orElseThrow(() -> new IllegalStateException("No underwriting result found for Lead ID: " + leadsId));

        BigDecimal maxApprovedAmount = latestResult.getApprovedAmount();
        Integer maxApprovedTenure = lead.getTenureMonths();

        BigDecimal selectedAmount = selectionDTO.getConfirmedAmount();
        Integer selectedTenure = selectionDTO.getConfirmedTenureMonths();

        if (selectedAmount == null || selectedTenure == null) {
            logger.warn("Invalid selection: amount or tenure is null.");
            throw new IllegalArgumentException("Amount and tenure must be provided.");
        }

        if (selectedAmount.compareTo(maxApprovedAmount) > 0) {
            logger.warn("Selected amount {} exceeds approved amount {}", selectedAmount, maxApprovedAmount);
            throw new IllegalArgumentException("Selected amount exceeds approved limit.");
        }

        if (selectedTenure > maxApprovedTenure) {
            logger.warn("Selected tenure {} exceeds approved tenure {}", selectedTenure, maxApprovedTenure);
            throw new IllegalArgumentException("Selected tenure exceeds approved limit.");
        }

        lead.setConfirmedAmount(selectedAmount);
        lead.setConfirmedTenureMonths(selectedTenure);
        lead.setStatus("Confirmed by Borrower");

        leadRepository.save(lead);

        logger.info("Lead ID {} updated with confirmed amount {} and tenure {}", leadsId, selectedAmount, selectedTenure);

        BorrowerSelectionDTO borrowerSelectionDTO = new BorrowerSelectionDTO();
        borrowerSelectionDTO.setConfirmedAmount(lead.getConfirmedAmount());
        borrowerSelectionDTO.setConfirmedTenureMonths(lead.getConfirmedTenureMonths());

        return borrowerSelectionDTO;
    }

    @Override
    public BorrowerSelectionDTO getLoanConfirmationByLeadId(Integer leadsId) {
        Lead lead = leadRepository.findById(leadsId)
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + leadsId));

        BorrowerSelectionDTO dto = new BorrowerSelectionDTO();
        dto.setConfirmedAmount(lead.getConfirmedAmount());
        dto.setConfirmedTenureMonths(lead.getConfirmedTenureMonths());

        return dto;
    }

}
