package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.KycDTO;

public interface KycService {
    String submitKyc(KycDTO kycDTO);
    // Get KYC by lead ID
    KycDTO getKycByLeadId(Integer leadId);

    // Delete KYC by KYC ID
    String deleteKycById(Integer kycId);
}

