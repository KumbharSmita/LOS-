package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.BankDetailsDTO;
import com.imperacred.BankLoanApplication.service.BankDetailsService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/bank")
public class BankDetailsController {

    private static final Logger logger = LogManager.getLogger(BankDetailsController.class);

    @Autowired
    private BankDetailsService bankDetailsService;

    @PostMapping("/{leadId}/bank-details")
    public ResponseEntity<BankDetailsDTO> submitBankDetails(
            @PathVariable Integer leadId,
            @RequestBody @Valid BankDetailsDTO dto) {

        logger.info("Received request to submit bank details for Lead ID: {}", leadId);
        logger.debug("Bank details received: Holder Name = {}, Account Number = {}, IFSC = {}",
                dto.getAccountHolderName(), dto.getAccountNumber(), dto.getIfscCode());

        BankDetailsDTO saved = bankDetailsService.saveBankDetails(leadId, dto);

        logger.info("Bank details saved successfully for Lead ID: {}", leadId);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping("/{leadId}/bank-details")
    public ResponseEntity<BankDetailsDTO> getBankDetails(@PathVariable Integer leadId) {
        logger.info("Fetching bank details for Lead ID: {}", leadId);
        BankDetailsDTO dto = bankDetailsService.getBankDetailsByLeadId(leadId);

        if (dto == null) {
            logger.info("No bank details found for Lead ID: {}", leadId);
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        logger.info("Bank details found for Lead ID: {}", leadId);
        return ResponseEntity.ok(dto);
    }

}
