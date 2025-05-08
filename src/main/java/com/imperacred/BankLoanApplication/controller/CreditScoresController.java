package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.CreditScoresDTO;
import com.imperacred.BankLoanApplication.service.CreditScoresService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit-scores")
public class CreditScoresController {

    @Autowired
    private CreditScoresService creditScoresService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAndSaveCreditScore(@Valid @RequestBody CreditScoresDTO dto) {
        CreditScoresDTO saved = creditScoresService.generateAndSaveScore(dto);
        String message = getLoanEligibilityMessage(saved.getScore());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private String getLoanEligibilityMessage(int score) {
        if (score < 700) {
            return "You are not eligible for a loan based on your credit score.";
        } else {
            return "You are eligible for a loan based on your credit score.";
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CreditScoresDTO> getCreditScoreById(@PathVariable int id) {
        CreditScoresDTO scoreDto = creditScoresService.getCreditScoreById(id);
        if (scoreDto != null) {
            return ResponseEntity.ok(scoreDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
