package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.model.LoanApplications;
import com.imperacred.BankLoanApplication.model.RepaymentSchedule;
import com.imperacred.BankLoanApplication.model.CreditScores;
import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.repository.LoanApplicationsRepository;
import com.imperacred.BankLoanApplication.repository.RepaymentScheduleRepository;
import com.imperacred.BankLoanApplication.repository.CreditScoresRepository;
import com.imperacred.BankLoanApplication.repository.DisbursementsRepository;
import com.imperacred.BankLoanApplication.repository.EmiRepository;
import com.imperacred.BankLoanApplication.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanApplicationsRepository loanApplicationsRepository;

    @Autowired
    private CreditScoresRepository creditScoreRepository;

    @Autowired
    private EmiRepository emiRepository;
    
    @Autowired
    private DisbursementsRepository disbursementRepository;

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

    
    @Override
    public BigDecimal calculateDynamicInterestRate(int creditScore) {
        BigDecimal baseInterestRate = new BigDecimal("12.00"); 
        if (creditScore >= 750) {
            return baseInterestRate.subtract(new BigDecimal("2.00")); 
        } else if (creditScore >= 650) {
            return baseInterestRate; 
        } else {
            return baseInterestRate.add(new BigDecimal("2.00")); 
        }
    }

   
    @Override
    public BigDecimal calculateEMI(BigDecimal principal, int tenureMonths, BigDecimal annualInterestRate) {
    	BigDecimal monthlyInterestRate = annualInterestRate
    		    .divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        // EMI Formula
        BigDecimal emiAmount = principal.multiply(monthlyInterestRate)
                                         .multiply(BigDecimal.ONE.add(monthlyInterestRate).pow(tenureMonths))
                                         .divide(BigDecimal.ONE.add(monthlyInterestRate).pow(tenureMonths).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        return emiAmount;
    }

    
    @Override
    public BigDecimal getCreditScoreForLoan(Integer applicationId) {
        CreditScores creditScore = creditScoreRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Credit score not found for application ID: " + applicationId));
        
        return new BigDecimal(creditScore.getScore());
    }
    
    @Transactional
    @Override
    public void createEMISchedule(LoanApplications loanApplication, BigDecimal emiAmount) {
        int totalInstallments = loanApplication.getTenure_months();
        LocalDate startDate = LocalDate.now().plusMonths(1); 
        LocalDate endDate = startDate.plusMonths(totalInstallments - 1);

        RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
        repaymentSchedule.setApplicationId(loanApplication.getApplication_id());
        repaymentSchedule.setTotalInstallments(totalInstallments);
        repaymentSchedule.setStartDate(startDate);
        repaymentSchedule.setEndDate(endDate);
        repaymentSchedule.setCreatedAt(LocalDateTime.now());
        repaymentSchedule.setUpdatedAt(LocalDateTime.now());

        repaymentScheduleRepository.save(repaymentSchedule);  // Save the schedule

        int scheduleId = repaymentSchedule.getScheduleId();

        BigDecimal monthlyEmiAmount = emiAmount;  // ✅ Fixed here

        for (int i = 0; i < totalInstallments; i++) {
            Emi emi = new Emi();
            emi.setApplication_id(loanApplication.getApplication_id());
            emi.setSchedule_id(scheduleId);  
            emi.setEmi_amount(monthlyEmiAmount);
            emi.setDue_date(startDate.plusMonths(i)); 
            emi.setPaid_date(null);  
            emi.setStatus("PENDING");
            emi.setCreated_at(LocalDateTime.now());
            emi.setUpdated_at(LocalDateTime.now());

            emiRepository.save(emi);
        }
    }

    


    

    
    @Transactional
    @Override
    public void processLoanApplication(LoanApplications loanApplication) {

        BigDecimal creditScore = getCreditScoreForLoan(loanApplication.getApplication_id());

       
        BigDecimal dynamicInterestRate = calculateDynamicInterestRate(creditScore.intValue());

    
        Disbursements disbursement = disbursementRepository.findByApplicationId(loanApplication.getApplication_id())
                .orElseThrow(() -> new RuntimeException("Disbursement not found for application ID: " + loanApplication.getApplication_id()));

        BigDecimal principal = disbursement.getDisbursedAmount();


        BigDecimal emiAmount = calculateEMI(principal, loanApplication.getTenure_months(), dynamicInterestRate);

     
        createEMISchedule(loanApplication, emiAmount);
    }


//	@Override
//	public List<Emi> getEmisByApplicationId(Integer applicationId) {
//		 return emiRepository.findByApplication_id(applicationId);
//	}

}
