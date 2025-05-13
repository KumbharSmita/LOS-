package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.dto.OtpRequestDTO;
import com.imperacred.BankLoanApplication.dto.OtpVerificationDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leads")
public class LeadsController {

    private static final Logger logger = LogManager.getLogger(LeadsController.class);

    @Autowired
    private LeadsService leadsService;
    
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
            .map(lead -> ResponseEntity.ok(Map.of(
                    "panNumber", lead.getPanNumber(),
                    "creditScore", lead.getCredit_score()
            )))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Lead not found")));
    }

}
