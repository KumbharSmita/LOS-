package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.model.LoanApplications;
import com.imperacred.BankLoanApplication.repository.EmiRepository;
import com.imperacred.BankLoanApplication.repository.LoanApplicationsRepository;
import com.imperacred.BankLoanApplication.service.LoanService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanApplicationsRepository loanApplicationsRepository;
    
    @Autowired
    private EmiRepository emiRepository;

    // Process the loan and generate EMI schedule
    @PostMapping("/process-loan/{applicationId}")
    public ResponseEntity<String> processLoan(@PathVariable Integer applicationId) {
       
        LoanApplications loanApplication = loanApplicationsRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Loan application not found"));

        
        loanService.processLoanApplication(loanApplication);

        return ResponseEntity.ok("Loan processed and EMI schedule created successfully.");
    }
    
   @GetMapping("/emi-schedule/{applicationId}")
    public ResponseEntity<List<Emi>> getEmiSchedule(@PathVariable Integer applicationId) {
        List<Emi> emis = emiRepository.findByApplicationId(applicationId);
        if (emis.isEmpty()) {
           return ResponseEntity.notFound().build();
        }
      return ResponseEntity.ok(emis);
  }
}
