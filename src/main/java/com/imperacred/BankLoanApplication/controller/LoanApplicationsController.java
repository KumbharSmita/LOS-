
package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.LoanApplicationsDTO;
import com.imperacred.BankLoanApplication.service.LoanApplicationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/loan-applications")
@RequiredArgsConstructor
public class LoanApplicationsController {

    private final LoanApplicationsService service;

    @GetMapping
    public List<LoanApplicationsDTO> getAll() {
        return service.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationsDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getApplicationById(id));
    }

    @PostMapping
    public ResponseEntity<LoanApplicationsDTO> create(@RequestBody LoanApplicationsDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createApplication(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanApplicationsDTO> updateApplication(
            @PathVariable Integer id,
            @RequestBody LoanApplicationsDTO dto) {
        return ResponseEntity.ok(service.updateApplication(id, dto));
    }

 

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        boolean deleted = service.deleteApplication(id);

        if (deleted) {
            return ResponseEntity.ok("Deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Loan application not found.");
        }
    }

 }