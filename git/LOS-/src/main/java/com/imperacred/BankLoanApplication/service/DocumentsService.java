package com.imperacred.BankLoanApplication.service;

import org.springframework.web.multipart.MultipartFile;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.model.Documents;

import java.util.List;

public interface DocumentsService {
	DocumentsDTO uploadDocument(Integer leadId, String documentType, MultipartFile file);

	List<DocumentsDTO> getDocumentsByLeadId(Integer leadId);

	Documents getDocumentById(Integer documentId);

	DocumentsDTO requestReupload(Integer leadsId, String documentType);

	boolean hasReuploadRequested(Integer leadsId);
}
