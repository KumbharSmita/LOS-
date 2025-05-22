package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

@RestController
@RequestMapping("/api/underwriting")
@CrossOrigin(origins = "http://localhost:5173") // Add this if you're calling from frontend
public class UnderwritingResultsController {

    private static final Logger logger = LogManager.getLogger(UnderwritingResultsController.class);

    @Autowired
    private UnderwritingResultsService underwritingService;

    @PostMapping("/underwrite")
    public ResponseEntity<UnderwritingResultsDTO> performUnderwriting(
            @RequestBody UnderwritingResultsDTO requestDTO) {

        logger.info("Received underwriting request for lead ID: {}", requestDTO.getLeadsId());

        UnderwritingResultsDTO result = underwritingService.performUnderwriting(
                requestDTO.getLeadsId(),
                requestDTO.getApprovedAmount()
        );

        return ResponseEntity.ok(result);
    }
    
    
    @GetMapping("/{leadsId}")
    public ResponseEntity<UnderwritingResultsDTO> getUnderwritingByLeadId(@PathVariable Integer leadsId) {
        logger.info("Fetching underwriting result for lead ID: {}", leadsId);
        UnderwritingResultsDTO result = underwritingService.getUnderwritingByLeadId(leadsId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UnderwritingResults>> getAllUnderwritingResults() {
        logger.info("Fetching all underwriting results");
        List<UnderwritingResults> results = underwritingService.getAllUnderwritingResults();
        return ResponseEntity.ok(results);
    }
}
