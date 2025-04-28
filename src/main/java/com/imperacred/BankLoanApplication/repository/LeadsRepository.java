package com.imperacred.BankLoanApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.dto.LeadsDTO;
import com.imperacred.BankLoanApplication.model.Leads;
//import com.imperacred.BankLoanApplication.model.Leads;

public interface LeadsRepository extends JpaRepository<Leads, Integer> {
	
	Optional<Leads> findById(Integer id);

	
}
