package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.SanctionLetterDTO;

import java.util.List;

public interface SanctionLetterService {

    SanctionLetterDTO createSanctionLetter(SanctionLetterDTO dto);

    SanctionLetterDTO getSanctionLetterById(Integer id);

    List<SanctionLetterDTO> getAllSanctionLetters();

    boolean deleteSanctionLetter(Integer id);
}