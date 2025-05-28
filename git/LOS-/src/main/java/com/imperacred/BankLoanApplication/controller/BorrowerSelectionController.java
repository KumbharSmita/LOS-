package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.BorrowerSelectionDTO;
import com.imperacred.BankLoanApplication.service.BorrowerSelectionService;
import com.imperacred.BankLoanApplication.service.OtpService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/borrower-selection")
public class BorrowerSelectionController {

    private static final Logger logger = LogManager.getLogger(BorrowerSelectionController.class);
    private static final String OTP_TYPE_LOAN_CONFIRMATION = "LOAN_CONFIRMATION";
    @Autowired
    private BorrowerSelectionService borrowerSelectionService;
    
    @Autowired
    private OtpService otpService;

    @PostMapping("/generate-confirmation-otp/{leadsId}")
    public ResponseEntity<String> generateConfirmationOtp(@PathVariable Integer leadsId) {
        try {
            otpService.generateOtpForLead(leadsId, OTP_TYPE_LOAN_CONFIRMATION);
            return ResponseEntity.ok("OTP sent to your email for loan confirmation.");
        } catch (Exception e) {
            logger.error("Error generating OTP for loan confirmation", e);
            return ResponseEntity.status(500).body("Failed to generate OTP.");
        }
    }

    @PostMapping("/confirm-loan-selection/{leadsId}")
    public ResponseEntity<?> confirmLoanWithOtp(
            @PathVariable Integer leadsId,
            @RequestParam("otp") String otp,
            @RequestBody BorrowerSelectionDTO borrowerSelectionDTO) {

        boolean isOtpValid = otpService.verifyOtpForLead(leadsId, OTP_TYPE_LOAN_CONFIRMATION, otp);
        if (!isOtpValid) {
            logger.warn("Invalid OTP for loan confirmation, Lead ID: {}", leadsId);
            return ResponseEntity.status(401).body("Invalid or expired OTP.");
        }

        try {
            BorrowerSelectionDTO confirmed = borrowerSelectionService.confirmLoanSelection(leadsId, borrowerSelectionDTO);
            return ResponseEntity.ok(confirmed);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    
    
    @PostMapping("/resend-confirmation-otp/{leadsId}")
    public ResponseEntity<String> resendConfirmationOtp(@PathVariable Integer leadsId) {
        try {
            otpService.resendOtpForLead(leadsId, OTP_TYPE_LOAN_CONFIRMATION);
            return ResponseEntity.ok("OTP resent to your email.");
        } catch (Exception e) {
            logger.error("Error resending OTP for loan confirmation", e);
            return ResponseEntity.status(500).body("Failed to resend OTP.");
        }
    }

    @GetMapping("/{leadsId}")
    public ResponseEntity<BorrowerSelectionDTO> getLoanConfirmation(@PathVariable Integer leadsId) {
        try {
            BorrowerSelectionDTO dto = borrowerSelectionService.getLoanConfirmationByLeadId(leadsId);
            if (dto.getConfirmedAmount() == null && dto.getConfirmedTenureMonths() == null) {
            
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            logger.error("Lead not found error for Lead ID {}: {}", leadsId, e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

}
