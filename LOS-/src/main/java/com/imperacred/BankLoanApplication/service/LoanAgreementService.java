package com.imperacred.BankLoanApplication.service;



import java.util.List;

import com.imperacred.BankLoanApplication.dto.LoanAgreementDTO;

public interface LoanAgreementService {
    LoanAgreementDTO createLoanAgreement(LoanAgreementDTO dto);
    LoanAgreementDTO getLoanAgreementById(Integer id);
    List<LoanAgreementDTO> getAllLoanAgreements();
    LoanAgreementDTO updateLoanAgreement(Integer id, LoanAgreementDTO dto);
    void deleteLoanAgreement(Integer id);
}
