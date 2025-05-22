package com.imperacred.BankLoanApplication.service.impl;

import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.service.CreditScoresService;

import java.util.Random;

@Service
public class CreditScoreServiceImpl implements CreditScoresService {

    // Method to generate a random credit score between 500 and 900
    @Override
    public Integer fetchCreditScoreByPan(String panNumber) {
        // Creating an instance of Random
        Random random = new Random();
        
        // Generating a random number between 500 and 900 (inclusive)
        int creditScore = 500 + random.nextInt(401); // 401 because 900-500 + 1 = 401
        
        return creditScore;
    }
}
