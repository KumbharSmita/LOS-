package com.imperacred.BankLoanApplication.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.DisbursementsDTO;
import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.model.LoanApplications;
import com.imperacred.BankLoanApplication.model.RepaymentSchedule;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.repository.LoanApplicationsRepository;
import com.imperacred.BankLoanApplication.repository.RepaymentScheduleRepository;
import com.imperacred.BankLoanApplication.service.DisbursementsService;

import jakarta.transaction.Transactional;

@Service
public class DisbursementsServiceImpl implements DisbursementsService {

    @Autowired
    private LoanApplicationsRepository loanApplicationRepository;

    @Autowired
    private DisbursementsRepository disbursementsRepository;
    
    @Autowired

    private RepaymentScheduleRepository repaymentScheduleRepository;
    

    private static final BigDecimal PROCESSING_FEE_PERCENTAGE = new BigDecimal("0.02");

    @Override
    @Transactional
    public DisbursementsDTO disburseLoan(DisbursementsDTO disbursementsDTO) {
        
        LoanApplications loanApplications = loanApplicationRepository.findById(disbursementsDTO.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Loan application not found"));

        
        if (!loanApplications.getStatus().equals("APPROVED")) {
            throw new RuntimeException("Loan application is not approved");
        }

        
        BigDecimal processingFee = disbursementsDTO.getDisbursedAmount().multiply(PROCESSING_FEE_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP); // 2% processing fee

       
        BigDecimal actualAmountCredited = disbursementsDTO.getDisbursedAmount().subtract(processingFee);

        
        Disbursements disbursements = new Disbursements();
        disbursements.setApplicationId(disbursementsDTO.getApplicationId());
        disbursements.setDisbursedAmount(disbursementsDTO.getDisbursedAmount());
        disbursements.setProcessingFee(processingFee);
        disbursements.setActualAmountCredited(actualAmountCredited);
        disbursements.setBankAccount(disbursementsDTO.getBankAccount());  // Bank account from DTO
        disbursements.setUtrNumber(generateUtrNumber());
        disbursements.setDisbursedAt(LocalDateTime.now());
        disbursements.setStatus("SUCCESS");

        
        disbursements = disbursementsRepository.save(disbursements);

        disbursementsDTO.setProcessingFee(processingFee);
        disbursementsDTO.setActualAmountCredited(actualAmountCredited);

        disbursementsDTO.setUtrNumber(disbursements.getUtrNumber());
        disbursementsDTO.setDisbursedAt(disbursements.getDisbursedAt());
        disbursementsDTO.setStatus(disbursements.getStatus());

        
        // Repayments Schedule after disbursement
        int totalInstallments = loanApplications.getTenure_months();
        LocalDate startDate = LocalDate.now().plusMonths(1); //  start date of EMI is next month
        LocalDate endDate = startDate.plusMonths(totalInstallments - 1);

        RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
        repaymentSchedule.setApplicationId(loanApplications.getApplication_id());
        repaymentSchedule.setTotalInstallments(totalInstallments);
        repaymentSchedule.setStartDate(startDate);
        repaymentSchedule.setEndDate(endDate);
        repaymentSchedule.setCreatedAt(LocalDateTime.now());
        repaymentSchedule.setUpdatedAt(LocalDateTime.now());

        repaymentScheduleRepository.save(repaymentSchedule);
       
        return disbursementsDTO;
    }

   
    private String generateUtrNumber() {
    	
        return "UTR" + System.currentTimeMillis(); 
    }
}
