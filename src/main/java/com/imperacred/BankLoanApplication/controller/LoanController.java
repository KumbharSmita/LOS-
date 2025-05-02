package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.model.LoanApplications;
import com.imperacred.BankLoanApplication.repository.EmiRepository;
import com.imperacred.BankLoanApplication.repository.LoanApplicationsRepository;
import com.imperacred.BankLoanApplication.service.LoanService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private static final Logger logger = LogManager.getLogger(LoanController.class);

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanApplicationsRepository loanApplicationsRepository;

    @Autowired
    private EmiRepository emiRepository;

    // Process the loan and generate EMI schedule
    @PostMapping("/process-loan/{applicationId}")
    public ResponseEntity<String> processLoan(@PathVariable Integer applicationId) {
        try {
            logger.info("Starting the process for loan application ID: {}", applicationId);

            LoanApplications loanApplication = loanApplicationsRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Loan application not found"));

            logger.debug("Fetched loan application details for ID: {}", applicationId);

            loanService.processLoanApplication(loanApplication);

            logger.info("Loan processed successfully and EMI schedule created for application ID: {}", applicationId);

            return ResponseEntity.ok("Loan processed and EMI schedule created successfully.");
        } catch (RuntimeException e) {
            logger.error("Error processing loan application ID: {}", applicationId, e);
            return ResponseEntity.status(500).body("Error processing loan application: " + e.getMessage());
        }
    }

    // Get the EMI schedule for a loan application
    @GetMapping("/emi-schedule/{applicationId}")
    public ResponseEntity<List<Emi>> getEmiSchedule(@PathVariable Integer applicationId) {
        try {
            logger.info("Fetching EMI schedule for loan application ID: {}", applicationId);

            List<Emi> emis = emiRepository.findByApplicationId(applicationId);

            if (emis.isEmpty()) {
                logger.warn("No EMI records found for application ID: {}", applicationId);
                return ResponseEntity.notFound().build();
            }

            logger.info("Found {} EMI records for application ID: {}", emis.size(), applicationId);
            return ResponseEntity.ok(emis);
        } catch (Exception e) {
            logger.error("Error fetching EMI schedule for loan application ID: {}", applicationId, e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
