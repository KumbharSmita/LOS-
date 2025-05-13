package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.dto.OtpRequestDTO;
import com.imperacred.BankLoanApplication.dto.OtpVerificationDTO;
<<<<<<< HEAD
import com.imperacred.BankLoanApplication.service.LeadsService;
import com.imperacred.BankLoanApplication.service.impl.OtpVerificationStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
=======
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.CreditScoresService;
import com.imperacred.BankLoanApplication.service.LeadsService;
import com.imperacred.BankLoanApplication.service.impl.OtpVerificationStatus;

import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
>>>>>>> origin/feature2
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

<<<<<<< HEAD
@CrossOrigin(origins = "http://localhost:3000")
=======
import jakarta.validation.Valid;

>>>>>>> origin/feature2
@RestController
@RequestMapping("/api/leads")
public class LeadsController {

    private static final Logger logger = LogManager.getLogger(LeadsController.class);

    @Autowired
    private LeadsService leadsService;
<<<<<<< HEAD

    @PostMapping("/create")
    public ResponseEntity<?> createLead(@Valid @RequestBody LeadsDTO leadDTO) {
        logger.info("Received Create Lead Request: {}", sanitizeLead(leadDTO));

        try {
            int leadId = leadsService.createLead(leadDTO);
            String response = "Lead created and OTP sent successfully.";
            logger.info("Response: {} | leadsId: {}", response, leadId);
            return ResponseEntity.ok().body("{\"leads_id\":" + leadId + ",\"message\":\"" + response + "\"}");
        } catch (Exception e) {
            logger.error("Error while creating lead: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("{\"error\":\"Failed to create lead or send OTP.\"}");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationDTO request) {
        logger.info("Received OTP verification request: {}", request);

        try {
            String result = leadsService.verifyOtp(request.getLeads_id(), request.getOtp_value());
            logger.info("OTP verification result: {}", result);

            if (OtpVerificationStatus.SUCCESS.getMessage().equals(result)) {
                return ResponseEntity.ok("{\"message\":\"OTP verified successfully.\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"message\":\"" + result + "\"}");
            }
        } catch (Exception e) {
            logger.error("Error verifying OTP: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("{\"error\":\"Internal error during OTP verification.\"}");
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody OtpRequestDTO request) {
        logger.info("Received OTP resend request: {}", request);

        try {
            String result = leadsService.resendOtp(request.getEmail());
            logger.info("Resend OTP Result: {}", result);
            return ResponseEntity.ok("{\"message\":\"" + result + "\"}");
        } catch (Exception e) {
            logger.error("Error resending OTP: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("{\"error\":\"Failed to resend OTP.\"}");
        }
    }

    // Mask sensitive info in logs
    private LeadsDTO sanitizeLead(LeadsDTO lead) {
        lead.setAadhaarNumber(mask(lead.getAadhaarNumber(), 4));
        lead.setPanNumber(mask(lead.getPanNumber(), 2));
        return lead;
    }

    private String mask(String value, int visibleDigits) {
        if (value == null || value.length() <= visibleDigits) return "****";
        return "*".repeat(value.length() - visibleDigits) + value.substring(value.length() - visibleDigits);
=======
    
    @Autowired
    private LeadsRepository leadRepository;

    @Autowired
    private CreditScoresService creditScoreService; // Interface injected

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
    
 // Submit a new lead and generate credit score
    @PostMapping("/submit")
    public ResponseEntity<?> submitLead(@RequestBody Lead lead) {
        if (lead.getPanNumber() == null || lead.getPanNumber().length() != 10) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid PAN number"));
        }

        Optional<Lead> existing = leadRepository.findByPanNumber(lead.getPanNumber());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Lead with this PAN already exists"));
        }

        int score = creditScoreService.generateCreditScore(lead.getPanNumber());
        lead.setCredit_score(score);

        Lead saved = leadRepository.save(lead);
        return ResponseEntity.ok(saved);
    }

    // Get credit score by PAN number
    @GetMapping("/credit-score")
    public ResponseEntity<?> getCreditScore(@RequestParam String panNumber) {
        return leadRepository.findByPanNumber(panNumber)
            .map(lead -> {
                int score = lead.getCredit_score(); // use correct getter method
                String eligibility = (score >= 700) ? "You are eligible" : "You are not eligible";

                return ResponseEntity.ok(Map.of(
                        "panNumber", lead.getPanNumber(),
                        "creditScore", score,
                        "eligibility", eligibility
                ));
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Lead not found")));
>>>>>>> origin/feature2
    }

}
