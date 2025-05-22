package com.imperacred.BankLoanApplication.service;

import java.math.BigDecimal;
import java.util.List;

import com.imperacred.BankLoanApplication.model.Disbursements;
import com.imperacred.BankLoanApplication.model.Emi;
import com.imperacred.BankLoanApplication.model.Lead;

public interface LoanService {

	BigDecimal calculateEMI(BigDecimal principal, int tenureMonths, BigDecimal annualInterestRate);

	void createEMISchedule(Lead leads, BigDecimal emiAmount, Disbursements disbursements);

	void processLoanApplication(Lead lead);

	List<Emi> getEmisByLeadsId(Integer leadsId);

}
