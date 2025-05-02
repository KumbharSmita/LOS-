package com.imperacred.BankLoanApplication.service;

import java.math.BigDecimal;
import java.util.List;

import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.model.LoanApplications;

public interface LoanService {
   
    
   
    BigDecimal calculateEMI(BigDecimal principal, int tenureMonths, BigDecimal annualInterestRate);

   
    
    
    void createEMISchedule(LoanApplications loanApplication, BigDecimal emiAmount,Disbursements disbursements);

  
    void processLoanApplication(LoanApplications loanApplication);
    
    
    List<Emi> getEmisByApplicationId(Integer applicationId);


	BigDecimal getBaseInterestRate();




	
}
