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
@CrossOrigin(origins = "http://localhost:3000")
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
        logger.info("Received request to disburse loan for leadsId: {}", disbursementsDTO.getLeadsId());

        try {
            // Disburse the loan
            DisbursementsDTO result = disbursementsService.disburseLoan(disbursementsDTO);
            logger.info("Loan successfully disbursed for leadsId: {} with UTR: {}", 
                        disbursementsDTO.getLeadsId(), result.getUtrNumber());
            return result;
        } catch (Exception e) {
            logger.error("Error while disbursing loan for leadsId: {}: {}", 
                         disbursementsDTO.getLeadsId(), e.getMessage(), e);
            throw e;  
        }
    }

   
    @GetMapping("/{leadsId}")
    public ResponseEntity<DisbursementsDTO> getDisbursementByLeadsId(@PathVariable Integer leadsId) {
        logger.info("Received request to fetch disbursement details for leadsId: {}", leadsId);

        try {
            // Fetch the disbursement details by applicationId
            Disbursements disbursement = disbursementsRepository.findByLeadsId(leadsId)
                .orElseThrow(() -> new RuntimeException("Disbursement not found for leads ID: " + leadsId));

            // Convert Disbursements entity to DTO
            DisbursementsDTO dto = new DisbursementsDTO();
            dto.setLeadsId(disbursement.getLeadsId());
            dto.setApprovedAmount(disbursement.getApprovedAmount());
            dto.setRateOfInterest(disbursement.getRateOfInterest());
            dto.setProcessingFee(disbursement.getProcessingFee());
            dto.setDisbursedAmount(disbursement.getDisbursedAmount());
            dto.setBankAccount(disbursement.getBankAccount());
            dto.setUtrNumber(disbursement.getUtrNumber());
            dto.setDisbursedAt(disbursement.getDisbursedAt());
            dto.setStatus(disbursement.getStatus());

            logger.info("Successfully fetched disbursement details for leadsId: {}", leadsId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error while fetching disbursement details for leadsId: {}: {}", leadsId, e.getMessage(), e);
            throw e; 
        }
    }
}
