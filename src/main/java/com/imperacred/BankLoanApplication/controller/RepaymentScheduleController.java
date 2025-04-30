package com.imperacred.BankLoanApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imperacred.BankLoanApplication.model.RepaymentSchedule;
import com.imperacred.BankLoanApplication.repository.RepaymentScheduleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/repayment-schedule")
public class RepaymentScheduleController {

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

  
    @GetMapping("/{applicationId}")
    public ResponseEntity<?> getRepaymentSchedule(@PathVariable Integer applicationId) {
        Optional repaymentSchedules = repaymentScheduleRepository.findByApplicationId(applicationId);

      
        if (repaymentSchedules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Repayment schedule not found for application ID: " + applicationId);
        }

        return ResponseEntity.ok(repaymentSchedules); 
    }
}
