package com.imperacred.BankLoanApplication.service;

import java.util.List;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;

public interface LeadsService {
	
		LeadsDTO saveLead(LeadsDTO leadsdto);
		LeadsDTO getLeadById(Integer id);
	    List<LeadsDTO> getAllLeads();
	    LeadsDTO updateLead(Integer id, LeadsDTO dto);
	    void deleteLead(Integer id);


//	LeadsDTO updateLead(Integer Id, Leads leads);
	
}
