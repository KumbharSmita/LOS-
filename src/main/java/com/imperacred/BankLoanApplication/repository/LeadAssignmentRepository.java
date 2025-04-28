package com.imperacred.BankLoanApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.LeadAssignments;

public interface LeadAssignmentRepository extends JpaRepository<LeadAssignments,Integer>{

	
}
