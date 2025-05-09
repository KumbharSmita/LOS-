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
                    logger.error("Lead  with ID {} not found", disbursementsDTO.getLeadsId());
                    return new RuntimeException("Lead not found");
                });

        // Validate lead status
        if (!lead.getStatus().equals("APPROVED")) {
            logger.error("Lead {} is not approved, cannot proceed with disbursement", disbursementsDTO.getLeadsId());
            throw new RuntimeException("Lead is not approved");
        }

        // Calculate processing fee and actual amount credited
        BigDecimal processingFee = disbursementsDTO.getApprovedAmount().multiply(PROCESSING_FEE_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);  // 2% processing fee
        BigDecimal disbursedAmount = disbursementsDTO.getApprovedAmount().subtract(processingFee);

        // Log fee calculation
        logger.debug("Processing fee calculated: {} for disbursed amount: {}", processingFee, disbursementsDTO.getDisbursedAmount());

        // Create Disbursement
        Disbursements disbursements = new Disbursements();
        disbursements.setLeadsId(disbursementsDTO.getLeadsId());
        disbursements.setApprovedAmount(disbursementsDTO.getApprovedAmount());
        disbursements.setRateOfInterest(disbursementsDTO.getRateOfInterest());
        disbursements.setProcessingFee(processingFee);
        disbursements.setDisbursedAmount(disbursedAmount);
        disbursements.setBankAccount(disbursementsDTO.getBankAccount());  // Bank account from DTO
        disbursements.setUtrNumber(generateUtrNumber());
        disbursements.setDisbursedAt(LocalDateTime.now());
        disbursements.setStatus("SUCCESS");

    
        logger.debug("Creating disbursement with UTR number: {}", disbursements.getUtrNumber());

        // Save Disbursement
        disbursements = disbursementsRepository.save(disbursements);

        // Log disbursement saved successfully
        logger.info("Loan disbursement completed successfully for applicationId: {} with UTR: {}",
                disbursementsDTO.getLeadsId(), disbursements.getUtrNumber());

        // Set the details in the DTO to return
        disbursementsDTO.setProcessingFee(processingFee);
        disbursementsDTO.setDisbursedAmount(disbursedAmount);
        disbursementsDTO.setUtrNumber(disbursements.getUtrNumber());
        disbursementsDTO.setDisbursedAt(disbursements.getDisbursedAt());
        disbursementsDTO.setStatus(disbursements.getStatus());

        return disbursementsDTO;
    }

    
    private String generateUtrNumber() {
        String utrNumber = "UTR" + UUID.randomUUID().toString();
        logger.debug("Generated unique UTR number: {}", utrNumber);
        return utrNumber;
    }
}
