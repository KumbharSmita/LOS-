package com.imperacred.BankLoanApplication.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.LeadStatusRequestDTO;
import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.repository.UnderwritingResultsRepository;
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/underwriting")
public class UnderwritingResultsController {

    private static final Logger logger = LogManager.getLogger(UnderwritingResultsController.class);

    @Autowired
    private UnderwritingResultsService underwritingService;

    @Autowired
    private LeadsRepository leadsRepository;

    @Autowired
    private UnderwritingResultsRepository underwritingResultsRepository;

    
    @PostMapping("/underwrite")
    public ResponseEntity<UnderwritingResultsDTO> performUnderwriting(
            @RequestBody @Valid UnderwritingResultsDTO requestDTO,
            HttpServletRequest request) {

        logger.info("Received underwriting request - Lead ID: {}, Amount: {}, ROI: {}%, Tenure: {} months",
                requestDTO.getLeadsId(),
                requestDTO.getApprovedAmount(),
                requestDTO.getRateOfInterest(),
                requestDTO.getTenureMonths());

        
        logger.debug("Full underwriting request DTO: {}", requestDTO);

        UnderwritingResultsDTO result = underwritingService.performUnderwriting(
                requestDTO.getLeadsId(),
                requestDTO.getApprovedAmount(),
                requestDTO.getRateOfInterest(),
                requestDTO.getTenureMonths(),
                request);

        logger.info("Underwriting completed for Lead ID {}. Decision: {}, Tenure Saved: {} months",
                result.getLeadsId(),
                result.getDecision(),
                result.getTenureMonths());

        return ResponseEntity.ok(result);
    }

   
    @GetMapping("/{leadsId}")
    public ResponseEntity<UnderwritingResultsDTO> getUnderwritingByLeadId(@PathVariable Integer leadsId) {
        logger.info("Fetching underwriting result for Lead ID: {}", leadsId);
        UnderwritingResultsDTO result = underwritingService.getUnderwritingByLeadId(leadsId);
        return ResponseEntity.ok(result);
    }

    
    @GetMapping("/all")
    public ResponseEntity<List<UnderwritingResultsDTO>> getAllUnderwritingResults() {
        logger.info("Fetching all underwriting results");
        List<UnderwritingResultsDTO> results = underwritingService.getAllUnderwritingResults();
        return ResponseEntity.ok(results);
    }

   
    @PostMapping("/lead-status")
    public ResponseEntity<?> getLeadStatus(@RequestBody @Valid LeadStatusRequestDTO request) {
        logger.info("Checking lead and underwriting status for email: {}", request.getEmail());

        return leadsRepository.findByEmail(request.getEmail()).map(lead -> {
            List<UnderwritingResults> results = underwritingResultsRepository.findByLeadsId(lead.getLeadsId());

            if (results.isEmpty()) {
                logger.info("Underwriting not yet done for Lead ID: {}", lead.getLeadsId());
                return ResponseEntity.ok().body(
                        Map.of(
                                "underwritingDone", false,
                                "leadsId", lead.getLeadsId(),
                                "status", lead.getStatus()
                        ));
            }

            UnderwritingResults result = results.get(0);
            logger.info("Returning underwriting status for Lead ID {}: {}", lead.getLeadsId(), result.getDecision());

            return ResponseEntity.ok().body(
                    Map.of(
                            "underwritingDone", true,
                            "leadsId", result.getLeadsId(),
                            "decision", result.getDecision(),
                            "riskRating", result.getRiskRating(),
                            "approvedAmount", result.getApprovedAmount(),
                            "underwriterNotes", result.getUnderwriterNotes(),
                            "evaluatedAt", result.getEvaluatedAt(),
                            "agentId", result.getAgentId(),
                            "tenureMonths", result.getTenureMonths(),
                            "rateOfInterest", result.getRateOfInterest()
                    ));
        }).orElseGet(() -> {
            logger.warn("No lead found for email: {}", request.getEmail());
            return ResponseEntity.status(404).body(
                    Map.of("message", "Lead not found for email: " + request.getEmail()));
        });
    }
}
