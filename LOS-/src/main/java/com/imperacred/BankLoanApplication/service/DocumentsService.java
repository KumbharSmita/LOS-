package com.imperacred.BankLoanApplication.service;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;

import java.util.List;

public interface DocumentsService {
    DocumentsDTO createDocument(DocumentsDTO documentDTO);
    List<DocumentsDTO> getAllDocuments();
    DocumentsDTO getDocumentById(Integer id);
    void deleteDocument(Integer id);
}
