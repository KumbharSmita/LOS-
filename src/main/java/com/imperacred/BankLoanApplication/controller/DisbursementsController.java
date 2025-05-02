package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.DisbursementsDTO;
import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.service.DisbursementsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disbursements")
public class DisbursementsController {

    private static final Logger logger = LogManager.getLogger(DisbursementsController.class);

    @Autowired
    private DisbursementsService disbursementsService;

    @Autowired
    private DisbursementsRepository disbursementsRepository;

    
    @PostMapping("/disburse-loan")
    public DisbursementsDTO disburseLoan(@RequestBody DisbursementsDTO disbursementsDTO) {
        logger.info("Received request to disburse loan for applicationId: {}", disbursementsDTO.getApplicationId());

        try {
            // Disburse the loan
            DisbursementsDTO result = disbursementsService.disburseLoan(disbursementsDTO);
            logger.info("Loan successfully disbursed for applicationId: {} with UTR: {}", 
                        disbursementsDTO.getApplicationId(), result.getUtrNumber());
            return result;
        } catch (Exception e) {
            logger.error("Error while disbursing loan for applicationId: {}: {}", 
                         disbursementsDTO.getApplicationId(), e.getMessage(), e);
            throw e;  
        }
    }

   
    @GetMapping("/{applicationId}")
    public ResponseEntity<DisbursementsDTO> getDisbursementByApplicationId(@PathVariable int applicationId) {
        logger.info("Received request to fetch disbursement details for applicationId: {}", applicationId);

        try {
            // Fetch the disbursement details by applicationId
            Disbursements disbursement = disbursementsRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Disbursement not found for application ID: " + applicationId));

            // Convert Disbursements entity to DTO
            DisbursementsDTO dto = new DisbursementsDTO();
            dto.setApplicationId(disbursement.getApplicationId());
            dto.setDisbursedAmount(disbursement.getDisbursedAmount());
            dto.setProcessingFee(disbursement.getProcessingFee());
            dto.setActualAmountCredited(disbursement.getActualAmountCredited());
            dto.setBankAccount(disbursement.getBankAccount());
            dto.setUtrNumber(disbursement.getUtrNumber());
            dto.setDisbursedAt(disbursement.getDisbursedAt());
            dto.setStatus(disbursement.getStatus());

            logger.info("Successfully fetched disbursement details for applicationId: {}", applicationId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error while fetching disbursement details for applicationId: {}: {}", applicationId, e.getMessage(), e);
            throw e; 
        }
    }
}
