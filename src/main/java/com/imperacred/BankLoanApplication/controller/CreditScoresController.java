////package com.imperacred.BankLoanApplication.controller;
////
////import com.imperacred.BankLoanApplication.dto.CreditScoresDTO;
////import com.imperacred.BankLoanApplication.service.CreditScoresService;
////import jakarta.validation.Valid;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.*;
////import org.springframework.web.bind.annotation.*;
////
////@RestController
////@RequestMapping("/api/credit-scores")
////public class CreditScoresController {
////
////    @Autowired
////    private CreditScoresService creditScoresService;
////
////    @PostMapping("/generate")
////    public ResponseEntity<String> generateAndSaveCreditScore(@Valid @RequestBody CreditScoresDTO dto) {
////        CreditScoresDTO saved = creditScoresService.generateAndSaveScore(dto);
////        String message = getLoanEligibilityMessage(saved.getScore());
////        return new ResponseEntity<>(message, HttpStatus.OK);
////    }
////
////    private String getLoanEligibilityMessage(int score) {
////        if (score < 700) {
////            return "You are not eligible for a loan based on your credit score.";
////        } else {
////            return "You are eligible for a loan based on your credit score.";
////        }
////    }
////    
////    @GetMapping("/{id}")
////    public ResponseEntity<CreditScoresDTO> getCreditScoreById(@PathVariable int id) {
////        CreditScoresDTO scoreDto = creditScoresService.getCreditScoreById(id);
////        if (scoreDto != null) {
////            return ResponseEntity.ok(scoreDto);
////        } else {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
////        }
////    }
////
////}
//
//package com.imperacred.BankLoanApplication.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//import com.imperacred.BankLoanApplication.model.Lead;
//import com.imperacred.BankLoanApplication.repository.LeadsRepository;
//import com.imperacred.BankLoanApplication.service.CreditScoresService;
//
//import java.util.*;
//
//@RestController
//@RequestMapping("/api/credit-score")
//public class CreditScoresController {
//
//    @Autowired
//    private LeadsRepository leadRepository;
//
//    @Autowired
//    private CreditScoresService creditScoreService;
//
//    // Generate or Fetch Credit Score by PAN number
//    @GetMapping
//    public ResponseEntity<?> getOrGenerateCreditScore(@RequestParam String panNumber) {
//        if (panNumber == null || panNumber.length() != 10) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Invalid PAN number"));
//        }
//
//        Optional<Lead> existingLead = leadRepository.findByPanNumber(panNumber);
//
//        if (existingLead.isPresent()) {
//            // Return existing credit score
//            Lead lead = existingLead.get();
//            return ResponseEntity.ok(Map.of(
//                    "panNumber", panNumber,
//                    "creditScore", lead.getCredit_score()
//            ));
//        } else {
//            // Generate new credit score and save as a new Lead (optional name)
//            int generatedScore = creditScoreService.generateCreditScore(panNumber);
//            Lead lead = new Lead(panNumber, generatedScore); // Default name
//
//            leadRepository.save(lead);
//
//            return ResponseEntity.ok(Map.of(
//                    "panNumber", panNumber,
//                    "creditScore", generatedScore
//            ));
//        }
//    }
//}
