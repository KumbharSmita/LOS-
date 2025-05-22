package com.imperacred.BankLoanApplication.service;

import java.util.List;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;

public interface DocumentsService {
    DocumentsDTO createDocument(DocumentsDTO dto);
    DocumentsDTO getDocumentById(Integer id);
    List<DocumentsDTO> getAllDocuments();
    void deleteDocument(Integer id);
}