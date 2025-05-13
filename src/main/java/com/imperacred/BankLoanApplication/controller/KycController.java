//package com.imperacred.BankLoanApplication.controller;
//
//import com.imperacred.BankLoanApplication.dto.KycDTO;
//import com.imperacred.BankLoanApplication.service.KycService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/kyc")
//public class KycController {
//
//    @Autowired
//    private KycService kycService;
//
//    @PostMapping("/submit")
//    public ResponseEntity<String> submitKyc(@RequestBody KycDTO kycDTO) {
//        String response = kycService.submitKyc(kycDTO);
//        return ResponseEntity.ok(response);
//    }
//    
//    //  Get KYC by leadId
//    @GetMapping("/{leadId}")
//    public ResponseEntity<KycDTO> getKycByLeadId(@PathVariable Integer leadId) {
//        KycDTO kycDTO = kycService.getKycByLeadId(leadId);
//        return ResponseEntity.ok(kycDTO);
//    }
//
//    //  Delete KYC by kycId
//    @DeleteMapping("/delete/{kycId}")
//    public ResponseEntity<String> deleteKyc(@PathVariable Integer kycId) {
//        String response = kycService.deleteKycById(kycId);
//        return ResponseEntity.ok(response);
//    }
//}
// 
