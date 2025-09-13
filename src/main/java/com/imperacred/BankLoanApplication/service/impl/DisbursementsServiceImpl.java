package com.imperacred.BankLoanApplication.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.DisbursementsDTO;
import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.DisbursementsService;

import jakarta.transaction.Transactional;

@Service
public class DisbursementsServiceImpl implements DisbursementsService {

    private static final Logger logger = LogManager.getLogger(DisbursementsServiceImpl.class);

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private DisbursementsRepository disbursementsRepository;

    private static final BigDecimal PROCESSING_FEE_PERCENTAGE = new BigDecimal("0.02");

    @Override
    @Transactional
    public DisbursementsDTO disburseLoan(DisbursementsDTO disbursementsDTO) {
        logger.info("Starting loan disbursement for applicationId: {}", disbursementsDTO.getLeadsId());

        // Check uniqueness of lead ID
        if (disbursementsRepository.existsByLeadsId(disbursementsDTO.getLeadsId())) {
            logger.error("Disbursement already exists for leadsId: {}", disbursementsDTO.getLeadsId());
            throw new RuntimeException("Disbursement already exists for this lead ID.");
        }

        // Check uniqueness of bank account
        if (disbursementsRepository.existsByBankAccount(disbursementsDTO.getBankAccount())) {
            logger.error("Bank account {} already used for another disbursement", disbursementsDTO.getBankAccount());
            throw new RuntimeException("Bank account already used.");
        }

        // Fetch loan application
        Lead lead = leadsRepository.findById(disbursementsDTO.getLeadsId())
                .orElseThrow(() -> {
                    logger.error("Lead with ID {} not found", disbursementsDTO.getLeadsId());
                    return new RuntimeException("Lead not found");
                });

        // Ensure the loan has been confirmed by the borrower
        if (!"Confirmed by Borrower".equalsIgnoreCase(lead.getStatus())) {
            logger.error("Lead {} is not confirmed by borrower", disbursementsDTO.getLeadsId());
            throw new RuntimeException("Borrower has not confirmed loan selection.");
        }

        // Ensure confirmed amount and tenure are set
        if (lead.getConfirmedAmount() == null || lead.getConfirmedTenureMonths() == null) {
            logger.error("Confirmed loan values not set for lead {}", disbursementsDTO.getLeadsId());
            throw new RuntimeException("Confirmed amount or tenure missing.");
        }

        // Calculate processing fee and disbursed amount
        BigDecimal confirmedAmount = lead.getConfirmedAmount();
        BigDecimal processingFee = confirmedAmount.multiply(PROCESSING_FEE_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal disbursedAmount = confirmedAmount.subtract(processingFee);

        // Log fee calculation
        logger.debug("Processing fee: {}, Disbursed amount: {}", processingFee, disbursedAmount);

        // Create disbursement record
        Disbursements disbursements = new Disbursements();
        disbursements.setLeadsId(disbursementsDTO.getLeadsId());
        disbursements.setApprovedAmount(confirmedAmount);
        disbursements.setRateOfInterest(disbursementsDTO.getRateOfInterest());
        disbursements.setProcessingFee(processingFee);
        disbursements.setDisbursedAmount(disbursedAmount);
        disbursements.setBankAccount(disbursementsDTO.getBankAccount());
        disbursements.setUtrNumber(generateUtrNumber());
        disbursements.setDisbursedAt(LocalDateTime.now());
        disbursements.setStatus("SUCCESS");

        // Save to database
        disbursements = disbursementsRepository.save(disbursements);

        logger.info("Disbursement successful for lead {} with UTR {}", disbursements.getLeadsId(), disbursements.getUtrNumber());

        // Set response DTO
        disbursementsDTO.setApprovedAmount(confirmedAmount);
        disbursementsDTO.setProcessingFee(processingFee);
        disbursementsDTO.setDisbursedAmount(disbursedAmount);
        disbursementsDTO.setUtrNumber(disbursements.getUtrNumber());
        disbursementsDTO.setDisbursedAt(disbursements.getDisbursedAt());
        disbursementsDTO.setStatus(disbursements.getStatus());

        return disbursementsDTO;
    }

    private String generateUtrNumber() {
        String utrNumber = "UTR" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        logger.debug("Generated UTR: {}", utrNumber);
        return utrNumber;
    }
}
