package com.imperacred.BankLoanApplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Integer> {

    //Optional<Otp> findTopByLead_LeadsIdOrderByCreatedAtDesc(String leadsId);

	//List<Otp> findByLead_LeadsIdOrderByCreatedAtDesc(String leadsId);

	

	Optional<Otp> findTopByOrderByCreatedAtDesc();




	Optional<Otp> findTopByLead_LeadsIdOrderByCreatedAtDesc(Integer leadsId);




	Optional<Otp> findByLead_LeadsId(Integer leadsId);
	

}
