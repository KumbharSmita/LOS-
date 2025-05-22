package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.dto.LeadVerificationResponseDTO;
import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.exception.DuplicateLeadException;
import com.imperacred.BankLoanApplication.mapper.LeadMapper;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.Otp;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.OtpRepository;
import com.imperacred.BankLoanApplication.service.*;
import com.imperacred.BankLoanApplication.util.ValidationUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LeadsServiceImpl implements LeadsService {

    private static final Logger logger = LogManager.getLogger(LeadsServiceImpl.class);
    private static final int OTP_EXPIRY_MINUTES = 2;

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CreditScoresService creditScoreService;

    @Autowired
    private LeadAssignmentService leadAssignmentService;

    @Override
    public LeadsDTO createLead(LeadsDTO leadDTO) {
        logger.info("Creating new lead with data: {}", leadDTO);

        // Validate inputs
        if (!ValidationUtil.isValidEmail(leadDTO.getEmail())) {
            logger.error("Invalid email format: {}", leadDTO.getEmail());
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!ValidationUtil.isValidAadhaarNumber(leadDTO.getAadhaarNumber())) {
            logger.error("Invalid Aadhaar number: {}", leadDTO.getAadhaarNumber());
            throw new IllegalArgumentException("Aadhaar number must be 12 digits.");
        }
        if (!ValidationUtil.isValidPhoneNumber(leadDTO.getPhone())) {
            logger.error("Invalid phone number: {}", leadDTO.getPhone());
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }

        // Check for duplicates
        if (leadsRepository.existsByEmailOrPhoneOrAadhaarNumberOrPanNumber(
                leadDTO.getEmail(), leadDTO.getPhone(),
                leadDTO.getAadhaarNumber(), leadDTO.getPanNumber())) {
            logger.warn("Duplicate lead detected with email: {}, phone: {}, Aadhaar: {}, PAN: {}", 
                leadDTO.getEmail(), leadDTO.getPhone(), leadDTO.getAadhaarNumber(), leadDTO.getPanNumber());
            throw new DuplicateLeadException("A lead with this email, phone, Aadhaar, or PAN already exists.");
        }

        // Create lead and save
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
        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        Otp otpEntity = new Otp();
        otpEntity.setOtpValue(otp);
        otpEntity.setLead(savedLead);
        otpEntity.setExpiryTime(expiryTime);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setUpdatedAt(LocalDateTime.now());
        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(savedLead.getEmail(), otp);
        logger.info("OTP {} sent to lead with email: {}", otp, savedLead.getEmail());

        return LeadMapper.toDto(savedLead);
    }

    @Override
    public LeadVerificationResponseDTO verifyOtp(Integer leadsId, String inputOtp) {
        logger.info("Verifying OTP for Lead ID: {}", leadsId);
        logger.info("Entered OTP: {}", inputOtp); // Log the OTP entered by the user

        Optional<Otp> optionalOtp = otpRepository.findTopByLead_LeadsIdOrderByCreatedAtDesc(leadsId);

        if (optionalOtp.isEmpty()) {
            logger.warn("No OTP found for Lead ID {}", leadsId);
            return new LeadVerificationResponseDTO("No OTP found for the provided Lead ID.", leadsId, null, null, null);
        }

        Otp otp = optionalOtp.get();

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            logger.warn("OTP expired for Lead ID {}: OTP expired at {}", leadsId, otp.getExpiryTime());
            return new LeadVerificationResponseDTO("OTP has expired for Lead ID " + leadsId + ". Please request a new OTP.", leadsId, null, null, null);
        }

        if (!otp.getOtpValue().equals(inputOtp)) {
            logger.warn("Invalid OTP for Lead ID {}: Expected {}, but got {}", leadsId, otp.getOtpValue(), inputOtp);
            return new LeadVerificationResponseDTO("Invalid OTP entered for Lead ID " + leadsId + ". Please try again.", leadsId, null, null, null);
        }

        // Proceed with lead update after successful OTP verification
        Optional<Lead> leadOpt = leadsRepository.findById(leadsId);
        if (leadOpt.isEmpty()) {
            logger.warn("Lead not found for Lead ID {}", leadsId);
            return new LeadVerificationResponseDTO("No lead found with the provided Lead ID: " + leadsId, leadsId, null, null, null);
        }

        Lead lead = leadOpt.get();

        if (lead.getCreditScore() != null) {
            lead.setStatus("LEAD GENERATED");
            leadsRepository.save(lead);

            // Assign agent
            LeadAssignmentResponseDTO assignmentDTO = leadAssignmentService.assignLeadToAgent(leadsId);
            LocalDateTime expectedContactTime = assignmentDTO.getAssigned_at().plusHours(2);

            lead.setStatus("LEAD ASSIGNED");
            leadsRepository.save(lead);

            String body = String.format(
                "Dear %s,\n\nYour loan application (Lead ID: %d) has been assigned to Agent ID: %d.\n" +
                "You can expect a call or email by: %s.\n\nThanks,\nImperaCred Team",
                lead.getFirstName(), leadsId, assignmentDTO.getAgent_id(), expectedContactTime
            );
            emailService.sendSimpleEmail(lead.getEmail(), "Agent Assigned", body);

            // Log agent assignment details
            logger.info("Lead ID {} assigned to Agent ID: {}. Expected contact time: {}", leadsId, assignmentDTO.getAgent_id(), expectedContactTime);

            return new LeadVerificationResponseDTO(
                "OTP verified successfully. Your application has been assigned to an agent.",
                leadsId,
                assignmentDTO.getAgent_id(),
                assignmentDTO.getAssigned_at(),
                expectedContactTime
            );
        } else {
            lead.setStatus("OTP VERIFIED");
            leadsRepository.save(lead);

            logger.info("OTP verified successfully for Lead ID {}. Credit score pending.", leadsId);

            return new LeadVerificationResponseDTO("OTP verified successfully. Credit score is pending.", leadsId, null, null, null);
        }
    }

    @Override
    public String resendOtp(String email) {
        logger.info("Resending OTP to email: {}", email);

        Optional<Lead> leadOpt = leadsRepository.findByEmail(email);
        if (leadOpt.isEmpty()) {
            logger.error("No lead found with email: {}", email);
            throw new RuntimeException("No lead found with this email.");
        }

        Lead lead = leadOpt.get();
        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        Optional<Otp> existingOtp = otpRepository.findByLead_LeadsId(lead.getLeadsId());
        Otp otpEntity = existingOtp.orElse(new Otp());

        otpEntity.setLead(lead);
        otpEntity.setOtpValue(otp);
        otpEntity.setExpiryTime(expiryTime);
        otpEntity.setUpdatedAt(LocalDateTime.now());
        if (otpEntity.getCreatedAt() == null) {
            otpEntity.setCreatedAt(LocalDateTime.now());
        }

        otpRepository.save(otpEntity);
        emailService.sendOtpEmail(email, otp);
        logger.info("OTP {} resent to email {} and expires at {}", otp, email, expiryTime);

        return "OTP resent successfully.";
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(100000 + random.nextInt(900000));
    }
}
