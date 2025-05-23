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
import com.imperacred.BankLoanApplication.service.EmailService;
import com.imperacred.BankLoanApplication.service.OtpService;
import com.imperacred.BankLoanApplication.util.ValidationUtil;

import jakarta.transaction.Transactional;

@Service
public class DisbursementsServiceImpl implements DisbursementsService {

    private static final Logger logger = LogManager.getLogger(DisbursementsServiceImpl.class);

    private static final BigDecimal PROCESSING_FEE_PERCENTAGE = new BigDecimal("0.02");
    private static final String OTP_TYPE_DISBURSEMENT_VERIFICATION = "DISBURSEMENT_VERIFICATION";

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private DisbursementsRepository disbursementsRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Override
    public void generateOtpForDisbursement(Integer leadId) {
        logger.info("Generating disbursement OTP for Lead ID: {}", leadId);
        String otp = otpService.generateOtpForLead(leadId, OTP_TYPE_DISBURSEMENT_VERIFICATION);

        Lead lead = leadsRepository.findById(leadId)
            .orElseThrow(() -> new RuntimeException("Lead not found for ID: " + leadId));

        String email = lead.getEmail();
        String name = lead.getFirstName();  

      
        emailService.sendDisbursementOtpEmail(email, name, otp, false);
        logger.info("Disbursement OTP sent to email: {}", email);
    }

    @Override
    public boolean verifyOtpForDisbursement(Integer leadId, String inputOtp) {
        logger.info("Verifying disbursement OTP for Lead ID: {}", leadId);
        boolean verified = otpService.verifyOtpForLead(leadId, OTP_TYPE_DISBURSEMENT_VERIFICATION, inputOtp);

        if (verified) {
            Lead lead = leadsRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found for ID: " + leadId));
            lead.setDisbursementOtpStatus("VERIFIED");
            leadsRepository.save(lead);
            logger.info("Disbursement OTP verified and status updated for lead ID: {}", leadId);
        }

        return verified;
    }

    @Override
    public void resendOtpForDisbursement(Integer leadId) {
        logger.info("Resending disbursement OTP for Lead ID: {}", leadId);
        String otp = otpService.resendOtpForLead(leadId, OTP_TYPE_DISBURSEMENT_VERIFICATION);

        Lead lead = leadsRepository.findById(leadId)
            .orElseThrow(() -> new RuntimeException("Lead not found for ID: " + leadId));

        String email = lead.getEmail();
        String name = lead.getFirstName();

        emailService.sendDisbursementOtpEmail(email, name, otp, true);
        logger.info("Disbursement OTP resent to email: {}", email);
    }

    @Transactional
    public DisbursementsDTO disburseLoan(DisbursementsDTO disbursementsDTO) {
        logger.info("Starting loan disbursement for leadId: {}", disbursementsDTO.getLeadsId());

        Lead lead = leadsRepository.findById(disbursementsDTO.getLeadsId())
            .orElseThrow(() -> new RuntimeException("Lead not found"));

        //  Check if disbursement OTP is verified
        if (!"VERIFIED".equalsIgnoreCase(lead.getDisbursementOtpStatus())) {
            logger.error("Disbursement OTP not verified for lead ID: {}", disbursementsDTO.getLeadsId());
            throw new RuntimeException("Disbursement OTP not verified.");
        }

        //  Check for duplicate disbursement or bank account
        if (disbursementsRepository.existsByLeadsId(disbursementsDTO.getLeadsId())) {
            throw new RuntimeException("Disbursement already exists for this lead ID.");
        }
  
        if (!ValidationUtil.isValidBankAccount(disbursementsDTO.getBankAccount())) {
            logger.error("Invalid bank account number: {}", disbursementsDTO.getBankAccount());
            throw new IllegalArgumentException("Invalid bank account number. It must be 9 to 18 digits.");
        }
        
        if (disbursementsRepository.existsByBankAccount(disbursementsDTO.getBankAccount())) {
            throw new RuntimeException("Bank account already used.");
        }

        // Confirm borrower & amount
        if (!"Confirmed by Borrower".equalsIgnoreCase(lead.getStatus())) {
            throw new RuntimeException("Borrower has not confirmed loan selection.");
        }

        if (lead.getConfirmedAmount() == null || lead.getConfirmedTenureMonths() == null) {
            throw new RuntimeException("Confirmed amount or tenure missing.");
        }

        BigDecimal confirmedAmount = lead.getConfirmedAmount();
        BigDecimal processingFee = confirmedAmount.multiply(PROCESSING_FEE_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal disbursedAmount = confirmedAmount.subtract(processingFee);

    
        Disbursements disbursements = new Disbursements();
        disbursements.setLeadsId(lead.getLeadsId());
        disbursements.setApprovedAmount(confirmedAmount);
        disbursements.setRateOfInterest(disbursementsDTO.getRateOfInterest());
        disbursements.setProcessingFee(processingFee);
        disbursements.setDisbursedAmount(disbursedAmount);
        disbursements.setBankAccount(disbursementsDTO.getBankAccount());
        disbursements.setUtrNumber(generateUtrNumber());
        disbursements.setDisbursedAt(LocalDateTime.now());
        disbursements.setStatus("SUCCESS");

        disbursementsRepository.save(disbursements);

      
        disbursementsDTO.setApprovedAmount(confirmedAmount);
        disbursementsDTO.setProcessingFee(processingFee);
        disbursementsDTO.setDisbursedAmount(disbursedAmount);
        disbursementsDTO.setUtrNumber(disbursements.getUtrNumber());
        disbursementsDTO.setDisbursedAt(disbursements.getDisbursedAt());
        disbursementsDTO.setStatus(disbursements.getStatus());

        logger.info("Disbursement successful for lead {} with UTR {}", lead.getLeadsId(), disbursements.getUtrNumber());
        return disbursementsDTO;
    }

    private String generateUtrNumber() {
        return "UTR" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
