package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.exception.DuplicateLeadException;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.Otp;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.OtpRepository;
import com.imperacred.BankLoanApplication.service.CreditScoresService;
import com.imperacred.BankLoanApplication.service.EmailService;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;
import com.imperacred.BankLoanApplication.service.LeadsService;
import com.imperacred.BankLoanApplication.util.ValidationUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LeadsServiceImpl implements LeadsService {

    private static final Logger logger = LogManager.getLogger(LeadsServiceImpl.class);

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


    private static final int OTP_EXPIRY_MINUTES = 2;

    @Override
    public int createLead(LeadsDTO leadDTO) {
        logger.info("Creating new lead with data: {}", leadDTO);

        if (!ValidationUtil.isValidEmail(leadDTO.getEmail())) {  // Use utility method for email validation
            logger.warn("Invalid email format: {}", leadDTO.getEmail());
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Validate Aadhaar Number Format (should be 12 digits)
        if (!ValidationUtil.isValidAadhaarNumber(leadDTO.getAadhaarNumber())) {  // Use utility method for Aadhaar validation
            logger.warn("Invalid Aadhaar number: {}", leadDTO.getAadhaarNumber());
            throw new IllegalArgumentException("Aadhaar number must be 12 digits.");
        }

        // Validate Phone Number Format (should be 10 digits)
        if (!ValidationUtil.isValidPhoneNumber(leadDTO.getPhone())) {  // Use utility method for phone validation
            logger.warn("Invalid phone number: {}", leadDTO.getPhone());
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }

        // Check for duplicate leads
        if (leadsRepository.existsByEmailOrPhoneOrAadhaarNumberOrPanNumber(
                leadDTO.getEmail(), leadDTO.getPhone(),
                leadDTO.getAadhaarNumber(), leadDTO.getPanNumber())) {
            logger.warn("Duplicate lead found with email: {}", leadDTO.getEmail());
            throw new DuplicateLeadException("A lead with this email, phone, Aadhaar, or PAN already exists.");
        }

        // Create Lead
        Lead lead = new Lead();
        lead.setFirstName(leadDTO.getFirstName());
        lead.setLastName(leadDTO.getLastName());
        lead.setEmail(leadDTO.getEmail());
        lead.setPhone(leadDTO.getPhone());
        lead.setPanNumber(leadDTO.getPanNumber());
        lead.setAadhaarNumber(leadDTO.getAadhaarNumber());
        lead.setSource(leadDTO.getSource());
        lead.setLoanType(leadDTO.getLoanType());
        lead.setAmount(leadDTO.getAmount());
        lead.setTenureMonths(leadDTO.getTenureMonths());
        lead.setPurpose(leadDTO.getPurpose());
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

        logger.info("OTP {} generated for email {} and expires at {}", otp, savedLead.getEmail(), expiryTime);
        return savedLead.getLeadsId();
    }

    @Override
    public String verifyOtp(Integer leadsId, String inputOtp) {
        Optional<Otp> optionalOtp = otpRepository.findTopByLead_LeadsIdOrderByCreatedAtDesc(leadsId);

        if (optionalOtp.isEmpty()) {
            logger.debug("No OTP found for lead ID: {}", leadsId);
            return OtpVerificationStatus.NOT_FOUND.getMessage();
        }

        Otp otp = optionalOtp.get();
        logger.debug("Stored OTP: {}, Entered OTP: {}", otp.getOtpValue(), inputOtp);

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            logger.debug("OTP expired at {}", otp.getExpiryTime());
            return OtpVerificationStatus.EXPIRED.getMessage();
        }

        if (!otp.getOtpValue().equals(inputOtp)) {
            logger.debug("OTP mismatch.");
            return OtpVerificationStatus.INVALID.getMessage();
        }

        // Log the OTP verification success
        logger.info("OTP successfully verified for lead ID: {}", leadsId);

        Optional<Lead> leadOpt = leadsRepository.findById(leadsId);
        if (leadOpt.isPresent()) {
            Lead lead = leadOpt.get();
            boolean creditScoreAvailable = lead.getCreditScore() != null;

            // Handle lead status based on credit score availability
            if (creditScoreAvailable) {
                lead.setStatus("LEAD GENERATED");
                leadsRepository.save(lead);

                logger.info("Lead ID {} has a valid credit score. Proceeding with lead assignment.", leadsId);

                // Call the lead assignment method
                leadAssignmentService.assignLeadToAgent(leadsId);

                // Log the lead assignment success after assigning to an agent
                lead.setStatus("LEAD ASSIGNED");
                leadsRepository.save(lead);

                logger.info("Lead ID {} successfully assigned to agent.", leadsId);
            } else {
                lead.setStatus("OTP VERIFIED");
                leadsRepository.save(lead);
                logger.info("Lead ID {} OTP verified. Credit score not yet available.", leadsId);
            }
        }

        return OtpVerificationStatus.SUCCESS.getMessage();
    }



    @Override
    public String resendOtp(String email) {
        logger.info("Resending OTP to email: {}", email);
        Optional<Lead> leadOpt = leadsRepository.findByEmail(email);
        if (leadOpt.isEmpty()) {
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
