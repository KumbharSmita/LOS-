package com.imperacred.BankLoanApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.service.DocumentsService;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentsController {

    @Autowired
    private DocumentsService documentsService;  // Updated to 'documentsService'

    @PostMapping
    public DocumentsDTO create(@RequestBody DocumentsDTO dto) {
        return documentsService.createDocument(dto);
    }

    @GetMapping("/{id}")
    public DocumentsDTO getById(@PathVariable Integer id) {
        return documentsService.getDocumentById(id);
    }

    @GetMapping
    public List<DocumentsDTO> getAll() {
        return documentsService.getAllDocuments();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        documentsService.deleteDocument(id);
    }
}