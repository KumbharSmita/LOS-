package com.imperacred.BankLoanApplication.service;

import org.springframework.web.multipart.MultipartFile;
import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import java.util.List;

public interface DocumentsService {
    DocumentsDTO uploadDocument(Integer leadId, String documentType, MultipartFile file);
    List<DocumentsDTO> getDocumentsByLeadId(Integer leadId);
}
