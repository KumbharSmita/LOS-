package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.LoanApplicationsDTO;

import java.util.List;

public interface LoanApplicationsService {
    List<LoanApplicationsDTO> getAllApplications();
    LoanApplicationsDTO getApplicationById(Integer id);
    LoanApplicationsDTO createApplication(LoanApplicationsDTO dto);
    LoanApplicationsDTO updateApplication(Integer id, LoanApplicationsDTO dto);
    boolean deleteApplication(Integer id);
}