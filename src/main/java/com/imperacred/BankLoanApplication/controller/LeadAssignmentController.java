package com.imperacred.BankLoanApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentRequestDTO;
import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/lead-assignments")
public class LeadAssignmentController {

 private final LeadAssignmentService leadAssignmentService;

 public LeadAssignmentController(LeadAssignmentService leadAssignmentService) {
     this.leadAssignmentService = leadAssignmentService;
 }

 @PostMapping("/assign")
 public ResponseEntity<LeadAssignmentResponseDTO> assignLead(@RequestBody @Valid LeadAssignmentRequestDTO request) {
     LeadAssignmentResponseDTO response = leadAssignmentService.assignLeadToAgent(request.getLeadsId());
     return ResponseEntity.ok(response);
 }
}
