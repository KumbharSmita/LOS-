
  package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imperacred.BankLoanApplication.dto.LeadStatusHistoryDTO;
import com.imperacred.BankLoanApplication.service.LeadStatusHistoryService;

@RestController
@RequestMapping("/api/lead-status-history") 
public class LeadsStatusHistoryController {

    @Autowired
    private LeadStatusHistoryService leadStatusHistoryService;
    @PostMapping
    public ResponseEntity<LeadStatusHistoryDTO> saveLeadStatusHistory(@RequestBody LeadStatusHistoryDTO dto) {
        LeadStatusHistoryDTO savedHistory = leadStatusHistoryService.saveLeadStatusHistory(dto);
        return new ResponseEntity<>(savedHistory, HttpStatus.CREATED);
    }
    @GetMapping("/lead/{leads_id}")
    public ResponseEntity<List<LeadStatusHistoryDTO>> getLeadStatusHistory(@PathVariable int leads_id) {
        List<LeadStatusHistoryDTO> historyList = leadStatusHistoryService.getLeadStatusHistory(leads_id);
        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }
    
    @GetMapping("/agent/{agent_id}")
    public ResponseEntity<List<LeadStatusHistoryDTO>> getAgentStatusHistory(@PathVariable int agent_id) {
        List<LeadStatusHistoryDTO> historyList = leadStatusHistoryService.getAgentStatusHistory(agent_id);
        return new ResponseEntity<>(historyList, HttpStatus.OK);
    }
    
 }
 