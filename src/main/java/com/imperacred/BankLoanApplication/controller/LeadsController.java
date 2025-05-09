package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.dto.OtpRequestDTO;
import com.imperacred.BankLoanApplication.dto.OtpVerificationDTO;
import com.imperacred.BankLoanApplication.service.LeadsService;
import com.imperacred.BankLoanApplication.service.impl.OtpVerificationStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leads")
public class LeadsController {

    private static final Logger logger = LogManager.getLogger(LeadsController.class);

    @Autowired
    private LeadsService leadsService;

    @PostMapping("/create")
    public ResponseEntity<String> createLead(@Valid @RequestBody LeadsDTO leadDTO) {
        logger.info("Received request to create lead with email: {}", leadDTO.getEmail());
        try {
            leadsService.createLead(leadDTO);
            logger.info("Lead created and OTP sent successfully for email: {}", leadDTO.getEmail());
            return ResponseEntity.ok("Lead created and OTP sent successfully.");
        } catch (Exception e) {
            logger.error("Error while creating lead for email: {}", leadDTO.getEmail(), e);
            return ResponseEntity.status(500).body("Failed to create lead or send OTP.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpVerificationDTO request) {
        logger.info("Received OTP verification request for lead ID: {}", request.getLeads_id());
        try {
            String result = leadsService.verifyOtp(request.getLeads_id(), request.getOtp_value());
            logger.info("OTP verification result for lead ID {}: {}", request.getLeads_id(), result);
            if (result.equals(OtpVerificationStatus.SUCCESS.getMessage())) {
                return ResponseEntity.ok("OTP verified successfully.");
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.error("Error during OTP verification for lead ID: {}", request.getLeads_id(), e);
            return ResponseEntity.status(500).body("Internal error during OTP verification.");
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@Valid @RequestBody OtpRequestDTO request) {
        logger.info("Received request to resend OTP to email: {}", request.getEmail());
        try {
            String result = leadsService.resendOtp(request.getEmail());
            logger.info("OTP resent successfully to email: {}", request.getEmail());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error while resending OTP to email: {}", request.getEmail(), e);
            return ResponseEntity.status(500).body("Failed to resend OTP.");
        }
    }
}
