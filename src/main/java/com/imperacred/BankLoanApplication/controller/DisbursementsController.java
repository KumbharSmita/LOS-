package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.DisbursementsDTO;
import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.service.DisbursementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disbursements")
public class DisbursementsController {

    @Autowired
    private DisbursementsService disbursementsService;
    
    @Autowired
    private DisbursementsRepository disbursementsRepository;

    @PostMapping("/disburse-loan")
    public DisbursementsDTO disburseLoan(@RequestBody DisbursementsDTO disbursementsDTO) {
        return disbursementsService.disburseLoan(disbursementsDTO);
    }
    
    @GetMapping("/{applicationId}")
    public ResponseEntity<DisbursementsDTO> getDisbursementByApplicationId(@PathVariable int applicationId) {
        Disbursements disbursement = disbursementsRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Disbursement not found for application ID: " + applicationId));

       
        DisbursementsDTO dto = new DisbursementsDTO();
        dto.setApplicationId(disbursement.getApplicationId());
        dto.setDisbursedAmount(disbursement.getDisbursedAmount());
        dto.setProcessingFee(disbursement.getProcessingFee());
        dto.setActualAmountCredited(disbursement.getActualAmountCredited());
        dto.setBankAccount(disbursement.getBankAccount());
        dto.setUtrNumber(disbursement.getUtrNumber());
        dto.setDisbursedAt(disbursement.getDisbursedAt());
        dto.setStatus(disbursement.getStatus());

        return ResponseEntity.ok(dto);
    }
}  
    

