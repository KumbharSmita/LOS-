//package com.imperacred.BankLoanApplication.controller;
//
<<<<<<< HEAD
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import com.imperacred.BankLoanApplication.dto.KycRequestDTO;
//import com.imperacred.BankLoanApplication.service.KycService;
=======
//import com.imperacred.BankLoanApplication.dto.KycDTO;
//import com.imperacred.BankLoanApplication.service.KycService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
>>>>>>> origin/feature2
//
//@RestController
//@RequestMapping("/api/kyc")
//public class KycController {
//
//    @Autowired
//    private KycService kycService;
//
<<<<<<< HEAD
//    // POST request to verify KYC using the request body
//    @PostMapping("/verify")
//    public boolean verifyKyc(@RequestBody KycRequestDTO kycRequest) {
//        return kycService.verifyKyc(
//            kycRequest.getLeadsId(),
//            kycRequest.getAadharNumber(),
//            kycRequest.getPanNumber()
//        );
//    }
//}
=======
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
>>>>>>> origin/feature2
