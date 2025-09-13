package com.imperacred.BankLoanApplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentRequestDTO;
import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/lead-assignments")
public class LeadAssignmentController {

    private static final Logger logger = LogManager.getLogger(LeadAssignmentController.class);

    private final LeadAssignmentService leadAssignmentService;

    public LeadAssignmentController(LeadAssignmentService leadAssignmentService) {
        this.leadAssignmentService = leadAssignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<LeadAssignmentResponseDTO> assignLead(@RequestBody @Valid LeadAssignmentRequestDTO request) {
        logger.info("Received lead assignment request for lead ID: {}", request.getLeadsId());

        try {
            LeadAssignmentResponseDTO response = leadAssignmentService.assignLeadToAgent(request.getLeadsId());
            logger.info("Lead successfully assigned: Lead ID {} -> Agent ID {}", response.getLeads_id(), response.getAgent_id());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to assign lead with ID {}: {}", request.getLeadsId(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
