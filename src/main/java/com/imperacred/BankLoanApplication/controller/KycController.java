//package com.imperacred.BankLoanApplication.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import com.imperacred.BankLoanApplication.dto.KycRequestDTO;
//import com.imperacred.BankLoanApplication.service.KycService;
//
//@RestController
//@RequestMapping("/api/kyc")
//public class KycController {
//
//    @Autowired
//    private KycService kycService;
//
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
