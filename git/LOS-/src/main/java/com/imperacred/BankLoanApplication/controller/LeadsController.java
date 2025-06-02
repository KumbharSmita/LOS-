package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.LeadVerificationResponseDTO;
import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.dto.OtpRequestDTO;
import com.imperacred.BankLoanApplication.dto.OtpVerificationDTO;
import com.imperacred.BankLoanApplication.service.LeadsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/leads")
public class LeadsController {

    private static final Logger logger = LogManager.getLogger(LeadsController.class);

    @Autowired
    private LeadsService leadsService;

    @PostMapping("/create")
    public ResponseEntity<?> createLead(@Valid @RequestBody LeadsDTO leadDTO) {
        logger.info("Received Create Lead Request: {}", leadDTO);
        try {
            LeadsDTO createdLead = leadsService.createLead(leadDTO);
            return ResponseEntity.ok(Map.of(
                "message", "Lead created and OTP sent successfully.",
                "lead", createdLead
            ));
        } catch (Exception e) {
            logger.error("Error while creating lead: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Failed to create lead or send OTP."));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationDTO request) {
        logger.info("Received OTP verification request: {}", request);

        try {
            LeadVerificationResponseDTO responseDTO = leadsService.verifyOtp(request.getLeadsId(), request.getOtpValue());

            
            if (responseDTO.getMessage().toLowerCase().contains("otp verified")) {
                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", responseDTO.getMessage()));
            }

        } catch (Exception e) {
            logger.error("Error verifying OTP for lead ID {}: {}", request.getLeadsId(), e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal error during OTP verification."));
        }
    }


    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody OtpRequestDTO request) {
        logger.info("Received OTP resend request: {}", request);

        try {
            String result = leadsService.resendOtp(request.getEmail());
            logger.info("Resend OTP Result: {}", result);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            logger.error("Error resending OTP: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Failed to resend OTP."));
        }
    }
}
