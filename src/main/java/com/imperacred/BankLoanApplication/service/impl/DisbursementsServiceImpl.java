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
import com.imperacred.BankLoanApplication.model.LoanApplications;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.repository.LoanApplicationsRepository;
import com.imperacred.BankLoanApplication.service.DisbursementsService;

import jakarta.transaction.Transactional;

@Service
public class DisbursementsServiceImpl implements DisbursementsService {

    private static final Logger logger = LogManager.getLogger(DisbursementsServiceImpl.class);

    @Autowired
    private LoanApplicationsRepository loanApplicationRepository;

    @Autowired
    private DisbursementsRepository disbursementsRepository;
    
    private static final BigDecimal PROCESSING_FEE_PERCENTAGE = new BigDecimal("0.02");

    @Override
    @Transactional
    public DisbursementsDTO disburseLoan(DisbursementsDTO disbursementsDTO) {
        logger.info("Starting loan disbursement for applicationId: {}", disbursementsDTO.getApplicationId());
        
        // Fetch loan application
        LoanApplications loanApplications = loanApplicationRepository.findById(disbursementsDTO.getApplicationId())
                .orElseThrow(() -> {
                    logger.error("Loan application with ID {} not found", disbursementsDTO.getApplicationId());
                    return new RuntimeException("Loan application not found");
                });

        // Validate loan status
        if (!loanApplications.getStatus().equals("APPROVED")) {
            logger.error("Loan application {} is not approved, cannot proceed with disbursement", disbursementsDTO.getApplicationId());
            throw new RuntimeException("Loan application is not approved");
        }

        // Calculate processing fee and actual amount credited
        BigDecimal processingFee = disbursementsDTO.getDisbursedAmount().multiply(PROCESSING_FEE_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);  // 2% processing fee
        BigDecimal actualAmountCredited = disbursementsDTO.getDisbursedAmount().subtract(processingFee);

        // Log fee calculation
        logger.debug("Processing fee calculated: {} for disbursed amount: {}", processingFee, disbursementsDTO.getDisbursedAmount());

        // Create Disbursement
        Disbursements disbursements = new Disbursements();
        disbursements.setApplicationId(disbursementsDTO.getApplicationId());
        disbursements.setDisbursedAmount(disbursementsDTO.getDisbursedAmount());
        disbursements.setProcessingFee(processingFee);
        disbursements.setActualAmountCredited(actualAmountCredited);
        disbursements.setBankAccount(disbursementsDTO.getBankAccount());  // Bank account from DTO
        disbursements.setUtrNumber(generateUtrNumber());
        disbursements.setDisbursedAt(LocalDateTime.now());
        disbursements.setStatus("SUCCESS");

    
        logger.debug("Creating disbursement with UTR number: {}", disbursements.getUtrNumber());

        // Save Disbursement
        disbursements = disbursementsRepository.save(disbursements);

        // Log disbursement saved successfully
        logger.info("Loan disbursement completed successfully for applicationId: {} with UTR: {}",
                disbursementsDTO.getApplicationId(), disbursements.getUtrNumber());

        // Set the details in the DTO to return
        disbursementsDTO.setProcessingFee(processingFee);
        disbursementsDTO.setActualAmountCredited(actualAmountCredited);
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
