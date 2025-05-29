package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.service.DocumentsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentsController {

    private static final Logger logger = LogManager.getLogger(DocumentsController.class);

    private final DocumentsService documentsService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentsDTO> uploadDocument(
            @RequestParam Integer leadsId,
            @RequestParam String documentType,
            @RequestParam MultipartFile file
    ) {
        logger.info("Received upload request for leadId: {}, documentType: {}", leadsId, documentType);
        DocumentsDTO uploaded = documentsService.uploadDocument(leadsId, documentType, file);
        logger.info("Upload successful for documentId: {}", uploaded.getDocumentId());
        return ResponseEntity.ok(uploaded);
    }

    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<DocumentsDTO>> getDocuments(@PathVariable Integer leadId) {
        logger.info("Received request to fetch documents for leadId: {}", leadId);
        List<DocumentsDTO> documents = documentsService.getDocumentsByLeadId(leadId);
        logger.info("Returning {} documents for leadId: {}", documents.size(), leadId);
        return ResponseEntity.ok(documents);
    }
}
