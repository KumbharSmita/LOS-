package com.imperacred.BankLoanApplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.model.Documents;
import com.imperacred.BankLoanApplication.repository.DocumentsRepository;
import com.imperacred.BankLoanApplication.service.DocumentsService;

/**
 * Service implementation for handling document operations.
 */
@Service
public class DocumentsServiceImpl implements DocumentsService {

    @Autowired
    private DocumentsRepository documentsRepository;

    // Converts DTO to Entity
    private Documents toEntity(DocumentsDTO dto) {
        return Documents.builder()
                .document_id(dto.getDocument_id())
                .leadsId(dto.getLeads_id())
                .document_type(dto.getDocument_type())
                .file_path(dto.getFile_path())
                .uploaded_at(dto.getUploaded_at())
                .build();
    }

    // Converts Entity to DTO
    private DocumentsDTO toDTO(Documents document) {
        return DocumentsDTO.builder()
                .document_id(document.getDocument_id())
                .leads_id(document.getLeadsId())
                .document_type(document.getDocument_type())
                .file_path(document.getFile_path())
                .uploaded_at(document.getUploaded_at())
                .build();
    }

    // Create and save document
    @Override
    public DocumentsDTO createDocument(DocumentsDTO dto) {
        Documents saved = documentsRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    // Retrieve document by ID
    @Override
    public DocumentsDTO getDocumentById(Integer id) {
        return documentsRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    // Retrieve all documents
    @Override
    public List<DocumentsDTO> getAllDocuments() {
        return documentsRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Delete document by ID
    @Override
    public void deleteDocument(Integer id) {
        documentsRepository.deleteById(id);
    }
}
