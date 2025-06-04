package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.LeadVerificationResponseDTO;
import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.exception.DuplicateLeadException;
import com.imperacred.BankLoanApplication.mapper.LeadMapper;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.repository.DocumentsRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.CreditScoresService;
import com.imperacred.BankLoanApplication.service.EmailService;
import com.imperacred.BankLoanApplication.service.LeadsService;
import com.imperacred.BankLoanApplication.service.OtpService;
import com.imperacred.BankLoanApplication.util.ValidationUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LeadsServiceImpl implements LeadsService {

    private static final Logger logger = LogManager.getLogger(LeadsServiceImpl.class);
    private static final String OTP_TYPE_LEAD_VERIFICATION = "LEAD_VERIFICATION";

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private DocumentsRepository documentsRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CreditScoresService creditScoreService;

    @Autowired
    private OtpService otpService;

    @Override
    public LeadsDTO createLead(LeadsDTO leadDTO) {
        logger.info("Creating new lead with data: {}", leadDTO);

        // Validate inputs
        if (!ValidationUtil.isValidEmail(leadDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!ValidationUtil.isValidAadhaarNumber(leadDTO.getAadhaarNumber())) {
            throw new IllegalArgumentException("Aadhaar number must be 12 digits.");
        }
        if (!ValidationUtil.isValidPhoneNumber(leadDTO.getPhone())) {
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }
        if (!ValidationUtil.isValidPanNumber(leadDTO.getPanNumber())) {
            throw new IllegalArgumentException("PAN number must be in the format: 5 letters, 4 digits, 1 letter.");
        }

        // Check for duplicates
        if (leadsRepository.existsByEmailOrPhoneOrAadhaarNumberOrPanNumber(
                leadDTO.getEmail(), leadDTO.getPhone(),
                leadDTO.getAadhaarNumber(), leadDTO.getPanNumber())) {
            throw new DuplicateLeadException("A lead with this email, phone, Aadhaar, or PAN already exists.");
        }

        // Create and save new lead
        Lead lead = LeadMapper.toEntity(leadDTO);
        lead.setStatus("NEW");
        lead.setCreatedAt(LocalDateTime.now());
        lead.setSubmittedAt(LocalDateTime.now());

        Integer creditScore = creditScoreService.fetchCreditScoreByPan(leadDTO.getPanNumber());
        lead.setCreditScore(creditScore);

        Lead savedLead = leadsRepository.save(lead);
        savedLead.setDisplayid("IBVL100" + savedLead.getLeadsId());
        leadsRepository.save(savedLead);

        // Generate OTP
        String otp = otpService.generateOtpForLead(savedLead.getLeadsId(), OTP_TYPE_LEAD_VERIFICATION);
        logger.info("OTP {} sent to lead with email: {}", otp, savedLead.getEmail());

        return LeadMapper.toDto(savedLead);
    }

    @Override
    public LeadVerificationResponseDTO verifyOtp(Integer leadsId, String inputOtp) {
        logger.info("Verifying OTP for Lead ID: {}", leadsId);

        boolean isValidOtp = otpService.verifyOtpForLead(leadsId, OTP_TYPE_LEAD_VERIFICATION, inputOtp);
        if (!isValidOtp) {
            return new LeadVerificationResponseDTO("Invalid or expired OTP.", leadsId, null, null, null);
        }

        Optional<Lead> leadOpt = leadsRepository.findById(leadsId);
        if (leadOpt.isEmpty()) {
            return new LeadVerificationResponseDTO("No lead found with the provided Lead ID: " + leadsId, leadsId, null, null, null);
        }

        Lead lead = leadOpt.get();

        if (lead.getCreditScore() != null) {
            if (lead.getCreditScore() < 700) {
                lead.setStatus("REJECTED");
                leadsRepository.save(lead);

                logger.info("Lead ID {} rejected due to low credit score: {}", leadsId, lead.getCreditScore());

                return new LeadVerificationResponseDTO(
                    "Your application is rejected due to low credit score (" + lead.getCreditScore() + ").",
                    leadsId,
                    null,
                    null,
                    null
                );
            } else {
                lead.setStatus("OTP VERIFIED");
                leadsRepository.save(lead);

                logger.info("OTP verified successfully for Lead ID {}. Awaiting document upload.", leadsId);

                return new LeadVerificationResponseDTO(
                    "OTP verified successfully. Please upload Salary Slip and Bank Statement to proceed.",
                    leadsId,
                    null,
                    null,
                    null
                );
            }
        } else {
            logger.info("OTP verified but credit score not found for Lead ID {}", leadsId);

            return new LeadVerificationResponseDTO(
                "OTP verified successfully. Credit score is pending.",
                leadsId,
                null,
                null,
                null
            );
        }
    }


    @Override
    public String resendOtp(String email) {
        Optional<Lead> leadOpt = leadsRepository.findByEmail(email);
        if (leadOpt.isEmpty()) {
            throw new RuntimeException("No lead found with this email.");
        }

        Lead lead = leadOpt.get();
        return otpService.resendOtpForLead(lead.getLeadsId(), OTP_TYPE_LEAD_VERIFICATION);
    }
    @Override
    public Optional<Lead> findLeadById(Integer leadsId) {
        return leadsRepository.findById(leadsId);
    }

}
