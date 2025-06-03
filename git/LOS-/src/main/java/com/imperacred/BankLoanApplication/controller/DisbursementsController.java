package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.DisbursementOtpDTO;
import com.imperacred.BankLoanApplication.dto.DisbursementsDTO;
import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.service.DisbursementsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/disbursements")
public class DisbursementsController {

    private static final Logger logger = LogManager.getLogger(DisbursementsController.class);

    @Autowired
    private DisbursementsService disbursementsService;

    @Autowired
    private DisbursementsRepository disbursementsRepository;

    @PostMapping("/disburse-loan")
    public ResponseEntity<DisbursementsDTO> disburseLoan(@RequestBody DisbursementsDTO disbursementsDTO) {
        logger.info("Received request to disburse loan for leadsId: {}", disbursementsDTO.getLeadsId());

        try {
            DisbursementsDTO result = disbursementsService.disburseLoan(disbursementsDTO);
            logger.info("Loan successfully disbursed for leadsId: {} with UTR: {}",
                    disbursementsDTO.getLeadsId(), result.getUtrNumber());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error while disbursing loan for leadsId: {}: {}", disbursementsDTO.getLeadsId(), e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{leadsId}")
    public ResponseEntity<DisbursementsDTO> getDisbursementByLeadsId(@PathVariable Integer leadsId) {
        logger.info("Received request to fetch disbursement details for leadsId: {}", leadsId);

        try {
            Disbursements disbursement = disbursementsRepository.findByLeadsId(leadsId)
                    .orElseThrow(() -> new RuntimeException("Disbursement not found for leads ID: " + leadsId));

            DisbursementsDTO dto = new DisbursementsDTO(
                    disbursement.getLeadsId(),
                    disbursement.getApprovedAmount(),
                    disbursement.getProcessingFee(),
                    disbursement.getDisbursedAmount(),
                    disbursement.getBankAccount(),
                    disbursement.getUtrNumber(),
                    disbursement.getDisbursedAt(),
                    disbursement.getStatus(),
                    null 
            );

            logger.info("Successfully fetched disbursement details for leadsId: {}", leadsId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error while fetching disbursement details for leadsId: {}: {}", leadsId, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestBody DisbursementOtpDTO request) {
        logger.info("Request to generate OTP for disbursement, leadsId: {}", request.getLeadsId());
        disbursementsService.generateOtpForDisbursement(request.getLeadsId());
        return ResponseEntity.ok("OTP sent successfully to registered email.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody DisbursementOtpDTO request) {
        logger.info("Verifying OTP for leadsId: {}", request.getLeadsId());
        boolean isValid = disbursementsService.verifyOtpForDisbursement(request.getLeadsId(), request.getOtpValue());
        return isValid 
            ? ResponseEntity.ok("OTP verified successfully.")
            : ResponseEntity.badRequest().body("Invalid or expired OTP.");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody DisbursementOtpDTO request) {
        logger.info("Request to resend OTP for leadsId: {}", request.getLeadsId());
        disbursementsService.resendOtpForDisbursement(request.getLeadsId());
        return ResponseEntity.ok("OTP resent successfully to registered email.");
    }
}
