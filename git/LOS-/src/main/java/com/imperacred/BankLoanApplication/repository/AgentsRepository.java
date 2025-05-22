package com.imperacred.BankLoanApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imperacred.BankLoanApplication.model.AgentLogin;
import com.imperacred.BankLoanApplication.model.Agents;
@Repository
public interface AgentsRepository extends JpaRepository<Agents,Integer> {

	 Optional<Agents> findByEmail(String email);

	Optional<Agents> findByAgentRegistrationId(int agent_registration_id);
}


