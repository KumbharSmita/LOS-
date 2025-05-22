package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;
import com.imperacred.BankLoanApplication.service.LeadStatusHistoryService;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lead-status-history")
@CrossOrigin(origins = "http://localhost:5173") 
public class LeadsStatusHistoryController {

    private static final Logger logger = LogManager.getLogger(LeadsStatusHistoryController.class);

    @Autowired
    private LeadStatusHistoryService leadStatusHistoryService;

    @PostMapping("/save")
    public ResponseEntity<?> createLeadStatusHistory(@RequestBody LeadStatusHistoryDTO dto,
                                                     HttpServletRequest request) {
        logger.info("POST /save called for lead ID: {}", dto.getLeads_id());
        try {
            LeadStatusHistoryDTO saved = leadStatusHistoryService.saveLeadStatusHistory(dto, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            logger.error("Error saving lead status history", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/lead/{leads_id}")
    public ResponseEntity<?> getHistoryByLead(@PathVariable Integer leads_id,
                                              HttpServletRequest request) {
        logger.info("GET /lead/{} called", leads_id);
        try {
            List<LeadStatusHistoryDTO> history = leadStatusHistoryService.getLeadStatusHistory(leads_id, request);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            logger.error("Error retrieving history for lead ID: {}", leads_id, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/agent/{agent_id}")
    public ResponseEntity<?> getHistoryByAgent(@PathVariable int agent_id,
                                               HttpServletRequest request) {
        logger.info("GET /agent/{} called", agent_id);
        try {
            List<LeadStatusHistoryDTO> history = leadStatusHistoryService.getAgentStatusHistory(agent_id, request);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            logger.error("Error retrieving history for agent ID: {}", agent_id, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
