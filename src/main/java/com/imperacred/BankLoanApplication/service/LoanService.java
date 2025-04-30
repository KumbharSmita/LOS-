package com.imperacred.BankLoanApplication.service;

import java.math.BigDecimal;
import java.util.List;

import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.model.LoanApplications;

public interface LoanService {
   
    BigDecimal calculateDynamicInterestRate(int creditScore);

   
    BigDecimal calculateEMI(BigDecimal principal, int tenureMonths, BigDecimal annualInterestRate);

   
    BigDecimal getCreditScoreForLoan(Integer applicationId);

    
    void createEMISchedule(LoanApplications loanApplication, BigDecimal emiAmount);

  
    void processLoanApplication(LoanApplications loanApplication);
    
    
//    List<Emi> getEmisByApplicationId(Integer applicationId);

}
