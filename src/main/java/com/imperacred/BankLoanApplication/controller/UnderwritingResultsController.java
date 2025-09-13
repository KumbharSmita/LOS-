package com.imperacred.BankLoanApplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

@RestController
@RequestMapping("/api/underwriting")
public class UnderwritingResultsController {

    private static final Logger logger = LogManager.getLogger(UnderwritingResultsController.class);

    @Autowired
    private UnderwritingResultsService underwritingService;

    @PostMapping("/perform/{leadsId}")
    public ResponseEntity<?> performUnderwriting(@PathVariable Integer leadsId) {
        logger.info("Received underwriting request for Lead ID: {}", leadsId);
        try {
            UnderwritingResultsDTO result = underwritingService.performUnderwriting(leadsId);
            logger.info("Underwriting completed successfully for Lead ID: {}", leadsId);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            logger.warn("Underwriting already performed for Lead ID: {}", leadsId);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Underwriting failed for Lead ID {}: {}", leadsId, e.getMessage());
            return ResponseEntity.internalServerError().body("Underwriting process failed.");
        }
    }
}
