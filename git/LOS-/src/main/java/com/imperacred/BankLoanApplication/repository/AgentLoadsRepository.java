package com.imperacred.BankLoanApplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.AgentLoads;

public interface AgentLoadsRepository extends JpaRepository<AgentLoads,Integer> {
	
	Optional<AgentLoads> findFirstByOrderByLeadCountAscLastAssignedAsc();

	List<AgentLoads> findAllByOrderByLeadCountAscLastAssignedAsc();
}


