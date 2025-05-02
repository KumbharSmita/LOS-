package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;
import com.imperacred.BankLoanApplication.service.LeadStatusHistoryService;

@RestController
@RequestMapping("/api/lead-status-history")
public class LeadsStatusHistoryController {

    private static final Logger logger = LogManager.getLogger(LeadsStatusHistoryController.class);

    @Autowired
    private LeadStatusHistoryService leadStatusHistoryService;

    @PostMapping
    public ResponseEntity<LeadStatusHistoryDTO> saveLeadStatusHistory(@RequestBody LeadStatusHistoryDTO dto) {
        logger.info("Request received to save lead status history: Lead ID {}, Agent ID {}, Status {}",
                dto.getLeads_id(), dto.getAgent_id(), dto.getStatus());

        LeadStatusHistoryDTO savedHistory = leadStatusHistoryService.saveLeadStatusHistory(dto);
        logger.info("Lead status history saved with ID: {}", savedHistory.getHistory_id());

        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }

    @GetMapping("/lead/{leads_id}")
    public ResponseEntity<List<LeadStatusHistoryDTO>> getLeadStatusHistory(@PathVariable int leads_id) {
        logger.info("Fetching status history for Lead ID: {}", leads_id);

        List<LeadStatusHistoryDTO> historyList = leadStatusHistoryService.getLeadStatusHistory(leads_id);
        logger.info("Found {} records for Lead ID: {}", historyList.size(), leads_id);

        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }

    @GetMapping("/agent/{agent_id}")
    public ResponseEntity<List<LeadStatusHistoryDTO>> getAgentStatusHistory(@PathVariable int agent_id) {
        logger.info("Fetching status history for Agent ID: {}", agent_id);

        List<LeadStatusHistoryDTO> historyList = leadStatusHistoryService.getAgentStatusHistory(agent_id);
        logger.info("Found {} records for Agent ID: {}", historyList.size(), agent_id);

        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }
}
