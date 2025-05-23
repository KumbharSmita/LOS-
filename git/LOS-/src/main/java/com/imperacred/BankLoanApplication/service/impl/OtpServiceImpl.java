package com.imperacred.BankLoanApplication.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.model.Otp;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.OtpRepository;
import com.imperacred.BankLoanApplication.service.EmailService;
import com.imperacred.BankLoanApplication.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LogManager.getLogger(OtpServiceImpl.class);
    private static final int OTP_EXPIRY_MINUTES = 2;

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    private String generateOtpValue() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    @Override
    public String generateOtpForLead(Integer leadId, String otpType) {
        Lead lead = leadsRepository.findById(leadId)
            .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

        String otp = generateOtpValue();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        Otp otpEntity = new Otp();
        otpEntity.setLead(lead);
        otpEntity.setOtpValue(otp);
        otpEntity.setExpiryTime(expiryTime);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setUpdatedAt(LocalDateTime.now());
        otpEntity.setOtpType(otpType);

        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(lead.getEmail(), otp);
        logger.info("Generated and sent OTP {} to lead with email: {}", otp, lead.getEmail());

        return otp;
    }

    @Override
    public boolean verifyOtpForLead(Integer leadId, String otpType, String inputOtp) {
        Optional<Otp> optionalOtp = otpRepository.findTopByLead_LeadsIdAndOtpTypeOrderByCreatedAtDesc(leadId, otpType);

        if (optionalOtp.isEmpty()) {
            logger.warn("No OTP found for leadId: {} and otpType: {}", leadId, otpType);
            return false;
        }

        Otp otp = optionalOtp.get();

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            logger.warn("OTP expired for leadId: {} and otpType: {}", leadId, otpType);
            return false;
        }

        boolean valid = otp.getOtpValue().equals(inputOtp);
        if (!valid) {
            logger.warn("Invalid OTP entered for leadId: {}, otpType: {}", leadId, otpType);
        }
        return valid;
    }

    @Override
    public String resendOtpForLead(Integer leadId, String otpType) {
        Lead lead = leadsRepository.findById(leadId)
            .orElseThrow(() -> new RuntimeException("Lead not found with id: " + leadId));

        String otp = generateOtpValue();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        Optional<Otp> existingOtp = otpRepository.findTopByLead_LeadsIdAndOtpTypeOrderByCreatedAtDesc(leadId, otpType);

        Otp otpEntity = existingOtp.orElse(new Otp());
        otpEntity.setLead(lead);
        otpEntity.setOtpValue(otp);
        otpEntity.setExpiryTime(expiryTime);
        otpEntity.setUpdatedAt(LocalDateTime.now());
        otpEntity.setOtpType(otpType);
        if (otpEntity.getCreatedAt() == null) {
            otpEntity.setCreatedAt(LocalDateTime.now());
        }

        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(lead.getEmail(), otp);
        logger.info("Resent OTP {} to lead with email: {}", otp, lead.getEmail());

        return "OTP resent successfully.";
    }
}
