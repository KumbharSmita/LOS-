package com.imperacred.BankLoanApplication.service;

<<<<<<< HEAD
import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
=======
import java.util.List;

import com.imperacred.BankLoanApplication.dto.UnderwritingResultsDTO;
import com.imperacred.BankLoanApplication.model.UnderwritingResults;
>>>>>>> origin/feature2

public interface UnderwritingResultsService {
    UnderwritingResults saveResult(UnderwritingResultsDTO dto);
    List<UnderwritingResults> getAllResults();
    UnderwritingResults getResultById(Integer id);
    UnderwritingResults updateResult(Integer id, UnderwritingResultsDTO dto);
    String deleteResult(Integer id);

	 UnderwritingResultsDTO performUnderwriting(Integer leadId);
}


