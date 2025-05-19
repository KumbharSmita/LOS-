package com.imperacred.BankLoanApplication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imperacred.BankLoanApplication.config.FileStorageProperties;
import com.imperacred.BankLoanApplication.dto.DocumentUploadMetadata;
import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.service.DocumentsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller for handling multiple document uploads.
 */
@RestController
@RequestMapping("/api/documents")
@Log4j2  // Log4j2 logger annotation from Lombok
public class DocumentsController {

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    /**
     * Uploads multiple documents along with their metadata.
     *
     * @param leadsId       ID of the lead to which documents belong.
     * @param metadataJson  JSON string containing metadata for each document.
     * @param files         Array of document files to be uploaded.
     * @return              List of uploaded document DTOs.
     * @throws IOException  If file writing fails.
     */
    @PostMapping("/upload-multiple")
    public List<DocumentsDTO> uploadMultipleDocuments(
            @RequestParam("leads_id") Integer leadsId,
            @RequestPart("metadata") String metadataJson,
            @RequestPart("files") MultipartFile[] files) throws IOException {

        log.info("Received document upload request for lead ID: {}", leadsId);

        ObjectMapper objectMapper = new ObjectMapper();
        List<DocumentUploadMetadata> metadataList = objectMapper.readValue(metadataJson, new TypeReference<>() {});

        if (metadataList.size() != files.length) {
            log.error("Metadata count ({}) does not match file count ({})", metadataList.size(), files.length);
            throw new IllegalArgumentException("Metadata count must match file count.");
        }

        String basePath = fileStorageProperties.getUploadDir();
        File leadFolder = new File(basePath + leadsId + File.separator);

        // Create directory for the lead if it doesn't exist
        if (!leadFolder.exists() && !leadFolder.mkdirs()) {
            log.error("Failed to create directory for lead: {}", leadsId);
            throw new IOException("Failed to create lead directory");
        }

        List<DocumentsDTO> uploadedDocuments = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            DocumentUploadMetadata metadata = metadataList.get(i);

            if (file.isEmpty()) {
                log.warn("Skipped empty file at index {}", i);
                continue;
            }

            // Extract file extension
            String ext = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(f.lastIndexOf(".")))
                    .orElse("");

            // Generate unique filename
            String filename = metadata.getDocumentType().replaceAll("[^a-zA-Z0-9]", "_").toLowerCase()
                    + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    + "_" + UUID.randomUUID().toString().substring(0, 8) + ext;

            File dest = new File(leadFolder, filename);
            file.transferTo(dest);
            log.info("Saved file {} to {}", file.getOriginalFilename(), dest.getAbsolutePath());

            // Build and persist document DTO
            DocumentsDTO doc = DocumentsDTO.builder()
                    .leads_id(leadsId)
                    .document_type(metadata.getDocumentType())
                    .document_category(metadata.getDocumentCategory())
                    .file_path(dest.getAbsolutePath())
                    .uploaded_at(LocalDateTime.now())
                    .build();

            uploadedDocuments.add(documentsService.createDocument(doc));
            log.info("Document metadata saved for file: {}", filename);
        }

        log.info("Total documents uploaded for lead {}: {}", leadsId, uploadedDocuments.size());
        return uploadedDocuments;
    }
}
