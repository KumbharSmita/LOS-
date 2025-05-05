package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
import com.imperacred.BankLoanApplication.service.UnderwritingResultsService;

@RestController
@RequestMapping("/api/underwriting")
public class UnderwritingResultsController {

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
    }
}
    
 

