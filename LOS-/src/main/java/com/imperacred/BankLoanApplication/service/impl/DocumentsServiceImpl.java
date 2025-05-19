package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.model.Documents;
import com.imperacred.BankLoanApplication.repository.DocumentsRepository;
import com.imperacred.BankLoanApplication.service.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentsServiceImpl implements DocumentsService {

    @Autowired
    private DocumentsRepository repo;

    @Override
    public DocumentsDTO createDocument(DocumentsDTO dto) {
        Documents doc = new Documents();
        doc.setLeads_id(dto.getLeads_id());
        doc.setDocument_type(dto.getDocument_type());
        doc.setDocument_category(dto.getDocument_category());
        doc.setFile_path(dto.getFile_path());
        doc.setUploaded_at(dto.getUploaded_at());

        doc = repo.save(doc);
        dto.setId(doc.getId());
        return dto;
    }

    @Override
    public List<DocumentsDTO> getAllDocuments() {
        return repo.findAll().stream().map(d -> DocumentsDTO.builder()
                .id(d.getId())
                .leads_id(d.getLeads_id())
                .document_type(d.getDocument_type())
                .document_category(d.getDocument_category())
                .file_path(d.getFile_path())
                .uploaded_at(d.getUploaded_at())
                .build()).collect(Collectors.toList());
    }

    @Override
    public DocumentsDTO getDocumentById(Integer id) {
        return repo.findById(id).map(d -> DocumentsDTO.builder()
                .id(d.getId())
                .leads_id(d.getLeads_id())
                .document_type(d.getDocument_type())
                .document_category(d.getDocument_category())
                .file_path(d.getFile_path())
                .uploaded_at(d.getUploaded_at())
                .build()).orElse(null);
    }

    @Override
    public void deleteDocument(Integer id) {
        repo.deleteById(id);
    }
}
