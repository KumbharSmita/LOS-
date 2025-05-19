package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.repository.EmiRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private static final Logger logger = LogManager.getLogger(LoanController.class);

    @Autowired
    private LoanService loanService;

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private EmiRepository emiRepository;

    // Process the loan and generate EMI schedule
    @PostMapping("/process-loan/{leadsId}")
    public ResponseEntity<String> processLoan(@PathVariable Integer leadsId) {
        if (leadsId == null || leadsId <= 0) {
            return ResponseEntity.badRequest().body("Invalid lead ID.");
        }
        try {
            logger.info("Starting the process for loan lead ID: {}", leadsId);

            Lead lead = leadsRepository.findById(leadsId)
                    .orElseThrow(() -> new IllegalArgumentException("Lead not found with ID: " + leadsId));

            logger.debug("Fetched leads details for ID: {}", leadsId);

            loanService.processLoanApplication(lead);

            logger.info("Loan processed successfully and EMI schedule created for lead ID: {}", leadsId);

            return ResponseEntity.ok("Loan processed and EMI schedule created successfully.");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument for lead ID: {}", leadsId, e);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing lead ID: {}", leadsId, e);
            return ResponseEntity.status(500).body("Error processing lead: " + e.getMessage());
        }
    }

    // Get the EMI schedule for a loan application
    @GetMapping("/emi-schedule/{leadsId}")
    public ResponseEntity<List<Emi>> getEmiSchedule(@PathVariable Integer leadsId) {
        try {
            logger.info("Fetching EMI schedule for lead ID: {}", leadsId);

            List<Emi> emis = emiRepository.findByLeadsId(leadsId);

            if (emis.isEmpty()) {
                logger.warn("No EMI records found for lead ID: {}", leadsId);
                return ResponseEntity.notFound().build();
            }

            logger.info("Found {} EMI records for lead ID: {}", emis.size(), leadsId);
            return ResponseEntity.ok(emis);
        } catch (Exception e) {
            logger.error("Error fetching EMI schedule for loan application ID: {}",leadsId, e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
