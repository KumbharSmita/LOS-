package com.imperacred.BankLoanApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.AgentRegistration;

public interface AgentRegistrationRepository extends JpaRepository<AgentRegistration, Integer> {
	Optional<AgentRegistration> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByContactno(String contactno);

}
