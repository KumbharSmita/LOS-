package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.model.LoanApplications;
import com.imperacred.BankLoanApplication.model.RepaymentSchedule;

import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.repository.LoanApplicationsRepository;
import com.imperacred.BankLoanApplication.repository.RepaymentScheduleRepository;

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
    private EmiRepository emiRepository;
    
    @Autowired
    private DisbursementsRepository disbursementRepository;

    @Autowired
    private RepaymentScheduleRepository repaymentScheduleRepository;

    
    @Override
    public BigDecimal getBaseInterestRate() {
       
         
            return new BigDecimal("12.00"); 
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

    
 

    
    @Transactional
    @Override
    public void createEMISchedule(LoanApplications loanApplication, BigDecimal emiAmount,Disbursements disbursements) {
        int totalInstallments = loanApplication.getTenure_months();
        LocalDate startDate = LocalDate.now().plusMonths(1); 
        LocalDate endDate = startDate.plusMonths(totalInstallments - 1);

        RepaymentSchedule repaymentSchedule = new RepaymentSchedule();
        repaymentSchedule.setApplicationId(loanApplication.getApplicationId());
        repaymentSchedule.setTotalInstallments(totalInstallments);
        repaymentSchedule.setStartDate(startDate);
        repaymentSchedule.setEndDate(endDate);
        
        BigDecimal totalAmount=emiAmount.multiply(new BigDecimal(loanApplication.getTenure_months()));
        
        BigDecimal totalInterest=totalAmount.subtract(disbursements.getDisbursedAmount());
        repaymentSchedule.setTotalAmount(totalAmount);
        repaymentSchedule.setTotalInterest(totalInterest);
        
       
        repaymentSchedule.setCreatedAt(LocalDateTime.now());
        repaymentSchedule.setUpdatedAt(LocalDateTime.now());

        repaymentScheduleRepository.save(repaymentSchedule);  // Save the schedule

        int scheduleId = repaymentSchedule.getScheduleId();

        BigDecimal monthlyEmiAmount = emiAmount;  

        for (int i = 0; i < totalInstallments; i++) {
            Emi emi = new Emi();
            emi.setApplicationId(loanApplication.getApplicationId());
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

      
       
       BigDecimal baseInterestRate=getBaseInterestRate();

    
        Disbursements disbursement = disbursementRepository.findByApplicationId(loanApplication.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Disbursement not found for application ID: " + loanApplication.getApplicationId()));

        BigDecimal principal = disbursement.getDisbursedAmount();


        BigDecimal emiAmount = calculateEMI(principal, loanApplication.getTenure_months(), baseInterestRate);

     
        createEMISchedule(loanApplication, emiAmount , disbursement);
    }


	@Override
	public List<Emi> getEmisByApplicationId(Integer applicationId) {
		 return emiRepository.findByApplicationId(applicationId);
	}

}
