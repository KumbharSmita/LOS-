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
import java.util.Map;
import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/leads")
public class LeadsController {

    private static final Logger logger = LogManager.getLogger(LeadsController.class);

    @Autowired
    private LeadsService leadsService;

    // POST /create - Create Lead and send OTP
    @PostMapping("/create")
    public ResponseEntity<?> createLead(@Valid @RequestBody LeadsDTO leadDTO) {
        logger.info("Received Create Lead Request: {}", leadDTO);

        try {
            int leadId = leadsService.createLead(leadDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("leads_id", leadId);
            response.put("message", "Lead created and OTP sent successfully.");
            response.put("lead", leadDTO); // Optionally include the lead data for feedback
            logger.info("Response: {} | leadsId: {}", response, leadId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while creating lead: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Failed to create lead or send OTP."));
        }
    }

    // POST /verify-otp - Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationDTO request) {
        logger.info("Received OTP verification request: {}", request);

        try {
            // First, verify the OTP and get the status message
            String result = leadsService.verifyOtp(request.getLeads_id(), request.getOtp_value());
            

            if (OtpVerificationStatus.SUCCESS.getMessage().equals(result)) {
                // If OTP is valid, proceed with success response
                return ResponseEntity.ok(Map.of("message", "OTP verified successfully."));
            } else {
                // If OTP is invalid or expired, return bad request with the result
                return ResponseEntity.badRequest().body(Map.of("message", result));
            }
        } catch (Exception e) {
            // Log any error encountered during OTP verification
            logger.error("Error verifying OTP for lead ID {}: {}", request.getLeads_id(), e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal error during OTP verification."));
        }
    }


    // POST /resend-otp - Resend OTP
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
