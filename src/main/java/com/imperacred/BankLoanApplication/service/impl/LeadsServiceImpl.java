package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.exception.DuplicateLeadException;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.Otp;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.OtpRepository;
import com.imperacred.BankLoanApplication.service.EmailService;
import com.imperacred.BankLoanApplication.service.LeadsService;
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

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void createLead(LeadsDTO leadDTO) {
        logger.info("Creating new lead with email: {}", leadDTO.getEmail());

        // Check if the lead already exists based on email, phone, Aadhaar, or PAN
        if (leadsRepository.existsByEmailOrPhoneOrAadhaarNumberOrPanNumber(
                leadDTO.getEmail(), leadDTO.getPhone(),
                leadDTO.getAadhaarNumber(), leadDTO.getPanNumber())) {
            logger.warn("Duplicate lead found with email: {}", leadDTO.getEmail());
            throw new DuplicateLeadException("A lead with this email, phone, Aadhaar, or PAN already exists.");
        }

        // Create a new Lead entity
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

        // Save the lead to the database and update its display ID
        Lead savedLead = leadsRepository.save(lead);  
        savedLead.setDisplayid("IBVL100" + savedLead.getLeadsId());
        leadsRepository.save(savedLead);
        logger.info("Lead created with ID: {}", savedLead.getLeadsId());

        // Generate OTP and send it to the email associated with the lead
        String otp = generateOtp();  
        Otp otpEntity = new Otp();
        otpEntity.setOtpValue(otp);
        otpEntity.setLead(savedLead);  
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(2));  
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setUpdatedAt(LocalDateTime.now());

        otpRepository.save(otpEntity);  
        emailService.sendOtpEmail(savedLead.getEmail(), otp);  
        logger.info("OTP generated and sent to email: {}", savedLead.getEmail());
    }

    @Override
    public String verifyOtp(Integer leadsId, String inputOtp) {
        Optional<Otp> optionalOtp = otpRepository.findTopByLead_LeadsIdOrderByCreatedAtDesc(leadsId);

        if (optionalOtp.isEmpty()) {
            logger.debug("No OTP found for lead ID: {}", leadsId);
            return OtpVerificationStatus.NOT_FOUND.getMessage();
        }

        Otp otp = optionalOtp.get();
        logger.debug("Stored OTP: {}", otp.getOtpValue());
        logger.debug("Entered OTP: {}", inputOtp);

        // Check if OTP has expired
        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            logger.debug("OTP expired. Expiry Time: {}", otp.getExpiryTime());
            return OtpVerificationStatus.EXPIRED.getMessage();
        }

        // Check if the entered OTP matches the stored OTP
        if (!otp.getOtpValue().equals(inputOtp)) {
            logger.debug("OTP mismatch. Stored OTP: {}, Entered OTP: {}", otp.getOtpValue(), inputOtp);
            return OtpVerificationStatus.INVALID.getMessage();
        }

        // OTP verified successfully, update the lead status to "OTP VERIFIED"
        Optional<Lead> leadOpt = leadsRepository.findById(leadsId);
        if (leadOpt.isPresent()) {
            Lead lead = leadOpt.get();
            lead.setStatus("OTP VERIFIED");  // Update status to OTP VERIFIED
            leadsRepository.save(lead);  // Save the updated lead
            logger.info("Lead ID {} status updated to OTP VERIFIED", leadsId);
        } else {
            logger.error("Lead not found for ID: {}", leadsId);
        }

        return OtpVerificationStatus.SUCCESS.getMessage();  // Return success message
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

        Optional<Otp> existingOtp = otpRepository.findByLead_LeadsId(lead.getLeadsId());
        Otp otpEntity = existingOtp.orElse(new Otp());
        otpEntity.setLead(lead);
        otpEntity.setOtpValue(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpEntity.setUpdatedAt(LocalDateTime.now());
        if (otpEntity.getCreatedAt() == null) {
            otpEntity.setCreatedAt(LocalDateTime.now());
        }

        otpRepository.save(otpEntity);
        emailService.sendOtpEmail(email, otp);
        logger.info("OTP resent to email: {}", email);
        return "OTP resent successfully.";
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);  
        logger.debug("Generated OTP: {}", otp);
        return String.valueOf(otp);
    }
}
