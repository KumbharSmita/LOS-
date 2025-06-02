package com.imperacred.BankLoanApplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.LeadAssignments;

public interface LeadAssignmentRepository extends JpaRepository<LeadAssignments,Integer>{

	List<LeadAssignments> findByAgentId(Integer agentId);
	boolean existsByLeadsIdAndAgentId(Integer leadsId, Integer agentId);

    Optional<LeadAssignments> findByLeadsIdAndAgentId(Integer leadsId, Integer agentId);
    boolean existsByLeadsIdAndStatus(Integer leadsId, String status);

    Optional<LeadAssignments> findByLeadsIdAndStatus(Integer leadsId, String status);
}
