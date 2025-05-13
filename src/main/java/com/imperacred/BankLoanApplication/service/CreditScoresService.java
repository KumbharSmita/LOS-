package com.imperacred.BankLoanApplication.service;

<<<<<<< HEAD
import com.imperacred.BankLoanApplication.dto.CreditScoresDTO;

public interface CreditScoresService {
    CreditScoresDTO generateAndSaveScore(CreditScoresDTO dto);
    CreditScoresDTO getCreditScoreById(int id);

}
=======

public interface CreditScoresService {
    int generateCreditScore(String panNumber);
}
>>>>>>> origin/feature2
