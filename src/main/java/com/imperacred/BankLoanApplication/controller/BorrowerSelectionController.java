package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.BorrowerSelectionDTO;
import com.imperacred.BankLoanApplication.service.BorrowerSelectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrower-selection")
public class BorrowerSelectionController {

    private static final Logger logger = LogManager.getLogger(BorrowerSelectionController.class);

    @Autowired
    private BorrowerSelectionService borrowerSelectionService;

    @PostMapping("/confirm-loan-selection/{leadsId}")
    public ResponseEntity<BorrowerSelectionDTO> confirmLoanSelection(
            @PathVariable Integer leadsId,
            @RequestBody BorrowerSelectionDTO borrowerSelectionDTO) {

        logger.info("Received loan selection confirmation request for Lead ID: {}", leadsId);

        try {
            BorrowerSelectionDTO confirmedSelection = borrowerSelectionService
                    .confirmLoanSelection(leadsId, borrowerSelectionDTO);

            logger.info("Loan selection confirmed for Lead ID: {} with Amount: {} and Tenure: {} months",
                    leadsId,
                    confirmedSelection.getConfirmedAmount(),
                    confirmedSelection.getConfirmedTenureMonths());

            return ResponseEntity.ok(confirmedSelection);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed for Lead ID {}: {}", leadsId, e.getMessage());
            return ResponseEntity.badRequest().body(new BorrowerSelectionDTO());
        } catch (RuntimeException e) {
            logger.error("Lead not found  error for Lead ID {}: {}", leadsId, e.getMessage());
            return ResponseEntity.status(404).body(new BorrowerSelectionDTO());
        }
    }
}
