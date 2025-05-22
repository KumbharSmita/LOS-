//package com.imperacred.BankLoanApplication.controller;
//
//import com.imperacred.BankLoanApplication.dto.CreditScoresDTO;
//import com.imperacred.BankLoanApplication.service.CreditScoresService;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/credit-scores")
//public class CreditScoresController {
//
//    private static final Logger logger = LogManager.getLogger(CreditScoresController.class);
//
//    @Autowired
//    private CreditScoresService creditScoresService;
//
//    @PostMapping("/generate")
//    public ResponseEntity<String> generateAndSaveCreditScore(@Valid @RequestBody CreditScoresDTO dto) {
//        logger.info("Received request to generate and save credit score for lead ID: {}", dto.getLeadsId());
//        
//        // Generate and save credit score
//        CreditScoresDTO saved = creditScoresService.generateAndSaveScore(dto);
//        
//        // Determine loan eligibility based on the score
//        String message = getLoanEligibilityMessage(saved.getScore());
//
//        logger.info("Credit score generated and saved for lead ID: {} with score: {}", dto.getLeadsId(), saved.getScore());
//        
//        return new ResponseEntity<>(message, HttpStatus.OK);
//    }
//
//    private String getLoanEligibilityMessage(int score) {
//        logger.debug("Evaluating loan eligibility for score: {}", score);
//        if (score < 700) {
//            logger.debug("Eligibility status: Not eligible for a loan.");
//            return "You are not eligible for a loan based on your credit score.";
//        } else {
//            logger.debug("Eligibility status: Eligible for a loan.");
//            return "You are eligible for a loan based on your credit score.";
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CreditScoresDTO> getCreditScoreById(@PathVariable int id) {
//        logger.info("Received request to fetch credit score for ID: {}", id);
//
//        // Fetch credit score by ID
//        CreditScoresDTO scoreDto = creditScoresService.getCreditScoreById(id);
//        
//        if (scoreDto != null) {
//            logger.info("Credit score found for ID: {}: {}", id, scoreDto);
//            return ResponseEntity.ok(scoreDto);
//        } else {
//            logger.warn("No credit score found for ID: {}", id);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//}
