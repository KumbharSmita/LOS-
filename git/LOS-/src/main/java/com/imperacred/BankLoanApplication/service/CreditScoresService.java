package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.CreditScoresDTO;

public interface CreditScoresService {
	  Integer fetchCreditScoreByPan(String panNumber);

}