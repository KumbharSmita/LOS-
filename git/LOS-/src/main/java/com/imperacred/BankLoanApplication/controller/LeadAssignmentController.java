package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.LeadAssignmentRequestDTO;
import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/lead-assignments")
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
            logger.info("Lead successfully assigned: Lead {} {} -> Agent ID {}",
                    response.getLead().getFirstName(),
                    response.getLead().getLastName(),
                    response.getAgent_id());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to assign lead with ID {}: {}", request.getLeadsId(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/assigned-leads/{agentId}")
    public ResponseEntity<List<LeadAssignmentResponseDTO>> getAssignedLeads(@PathVariable Integer agentId) {
        logger.info("Received request to fetch assigned leads for agent ID: {}", agentId);

        List<LeadAssignmentResponseDTO> leads = leadAssignmentService.getAssignedLeadsForAgent(agentId);

        logger.info("Returning {} leads for agent ID: {}", leads.size(), agentId);

        return ResponseEntity.ok(leads);
    }
    
    @GetMapping("/assigned-leads/{agentId}/status")
    public ResponseEntity<List<LeadAssignmentResponseDTO>> getAssignedLeadsByStatus(
            @PathVariable Integer agentId,
            @RequestParam String status) {

        logger.info("Request to filter assigned leads for agent {} with status: {}", agentId, status);

        List<LeadAssignmentResponseDTO> filteredLeads = leadAssignmentService
                .getAssignedLeadsForAgentByStatus(agentId, status);

        return ResponseEntity.ok(filteredLeads);
    }

}
