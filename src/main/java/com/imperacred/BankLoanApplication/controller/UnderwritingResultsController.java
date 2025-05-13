package com.imperacred.BankLoanApplication.controller;

<<<<<<< HEAD
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
=======
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
>>>>>>> origin/feature2
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

@RestController
@RequestMapping("/api/underwriting")
public class UnderwritingResultsController {

<<<<<<< HEAD
    private static final Logger logger = LogManager.getLogger(UnderwritingResultsController.class);

    @Autowired
    private UnderwritingResultsService underwritingService;

    @PostMapping("/perform/{leadsId}")
    public ResponseEntity<?> performUnderwriting(@PathVariable Integer leadsId) {
        logger.info("Received underwriting request for Lead ID: {}", leadsId);
        try {
            UnderwritingResultsDTO result = underwritingService.performUnderwriting(leadsId);
            logger.info("Underwriting completed successfully for Lead ID: {}", leadsId);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            logger.warn("Underwriting already performed for Lead ID: {}", leadsId);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Underwriting failed for Lead ID {}: {}", leadsId, e.getMessage());
            return ResponseEntity.internalServerError().body("Underwriting process failed.");
        }
=======
    @Autowired
    private UnderwritingResultsService service;

    @PostMapping("/result")
    public UnderwritingResults submitResult(@RequestBody UnderwritingResultsDTO dto) {
        return service.saveResult(dto);
    }
    
 //  Get all results
    @GetMapping("/results")
    public List<UnderwritingResults> getAllResults() {
        return service.getAllResults();
    }

    //  Get result by ID
    @GetMapping("/result/{id}")
    public UnderwritingResults getResultById(@PathVariable Integer id) {
        return service.getResultById(id);
    }

    // Update result by ID
    @PutMapping("/result/{id}")
    public UnderwritingResults updateResult(@PathVariable Integer id, @RequestBody UnderwritingResultsDTO dto) {
        return service.updateResult(id, dto);
    }

    // Delete result by ID
    @DeleteMapping("/result/{id}")
    public String deleteResult(@PathVariable Integer id) {
        return service.deleteResult(id);
>>>>>>> origin/feature2
    }
}
    
 

