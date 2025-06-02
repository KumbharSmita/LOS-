package com.imperacred.BankLoanApplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imperacred.BankLoanApplication.model.Documents;

public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

	List<Documents> findByLeadsId(Integer leadsId);
	boolean existsByLeadsIdAndDocumentType(Integer leadsId, String documentType);
	Optional<Documents> findByLeadsIdAndDocumentType(Integer leadsId, String documentType);
}
