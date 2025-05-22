package com.imperacred.BankLoanApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.LeadAssignments;

public interface LeadAssignmentRepository extends JpaRepository<LeadAssignments,Integer>{

	List<LeadAssignments> findByAgentId(Integer agentId);

}
