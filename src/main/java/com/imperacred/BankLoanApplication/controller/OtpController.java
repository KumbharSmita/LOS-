package com.imperacred.BankLoanApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import com.imperacred.BankLoanApplication.dto.*;
import com.imperacred.BankLoanApplication.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/otp")
//public class OtpController {
//
//    @Autowired
//    private OtpService otpService;
//    
//    @PostMapping("/generate")
//    public ResponseEntity<OtpResponse> generateOtp(@RequestBody OtpRequest otpRequest) {
//        OtpResponse response = otpService.generateOtp(otpRequest);
//        return ResponseEntity.ok(response);
//    }
//    
//    @PostMapping("/verify")
//    public ResponseEntity<OtpVerificationResponse> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest) {
//        OtpVerificationResponse response = otpService.verifyOtp(otpVerificationRequest);
//        return ResponseEntity.ok(response);
//    }
//    
//    // Scheduled task to clean up expired OTPs
//    // This could also be implemented as a scheduled task in the service
//    @DeleteMapping("/cleanup")
//    public ResponseEntity<String> cleanupExpiredOtps() {
//        otpService.cleanupExpiredOtps();
//        return ResponseEntity.ok("Expired OTPs cleaned up successfully");
//    }
//}

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<OtpDTO> generateOtp(@RequestBody OtpRequest request) {
        return ResponseEntity.ok(otpService.generateOtp(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<OtpDTO> verifyOtp(@RequestBody OtpVerificationRequest request) {
        return ResponseEntity.ok(otpService.verifyOtp(request));
    }
}



























//package com.imperacred.BankLoanApplication.controller;
//
//
//import com.imperacred.BankLoanApplication.dto.OtpDTO;
//import com.imperacred.BankLoanApplication.service.OtpService;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/otp")
//public class OtpController {
//
//    @Autowired
//    private OtpService otpService;
//
//    @PostMapping("/generate/{leadsId}")
//    public ResponseEntity<OtpDTO> generateOtp(@PathVariable Integer leadsId) {
//        OtpDTO otpDTO = otpService.generateOtp(leadsId);
//        return ResponseEntity.ok(otpDTO);
//    }
//}

