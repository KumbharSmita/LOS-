package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.CreditScoresDTO;

public interface CreditScoresService {
    CreditScoresDTO generateAndSaveScore(CreditScoresDTO dto);
    CreditScoresDTO getCreditScoreById(int id);

}
