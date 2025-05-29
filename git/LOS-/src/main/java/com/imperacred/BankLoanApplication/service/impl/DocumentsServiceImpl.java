package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.model.Documents;
import com.imperacred.BankLoanApplication.repository.DocumentsRepository;
import com.imperacred.BankLoanApplication.service.DocumentsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentsServiceImpl implements DocumentsService {

    private static final Logger logger = LogManager.getLogger(DocumentsServiceImpl.class);

    private final DocumentsRepository repository;

    @Override
    public DocumentsDTO uploadDocument(Integer leadId, String documentType, MultipartFile file) {
        logger.info("Starting uploadDocument for leadId: {}, documentType: {}", leadId, documentType);

        try {
            // Get user home directory dynamically
            String userHome = System.getProperty("user.home");
            String desktopUploadsDir = userHome + File.separator + "Desktop" + File.separator + "uploads";

            logger.info("Resolved desktop uploads directory: {}", desktopUploadsDir);

            // Ensure directory exists
            Files.createDirectories(Paths.get(desktopUploadsDir));
            logger.info("Upload directory verified/created at: {}", desktopUploadsDir);

            // Create file name
            String fileName = leadId + "_" + documentType + "_" + file.getOriginalFilename();
            String filePath = desktopUploadsDir + File.separator + fileName;

            logger.debug("Resolved fileName: {}", fileName);
            logger.debug("Full file path: {}", filePath);

            // Transfer the file to the destination path
            file.transferTo(new File(filePath));
            logger.info("File transferred to disk at {}", filePath);

            // Create document entity and save
            Documents doc = new Documents();
            doc.setLeadsId(leadId);
            doc.setDocumentType(documentType);
            doc.setFilePath(filePath);
            doc.setUploadedAt(LocalDateTime.now());

            logger.debug("Saving document entity to repository: {}", doc);
            Documents saved = repository.save(doc);
            logger.info("Document saved with ID: {}", saved.getDocumentId());

            return mapToDTO(saved);
        } catch (IOException e) {
            logger.error("Failed to store file for leadId: {}, documentType: {}", leadId, documentType, e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public List<DocumentsDTO> getDocumentsByLeadId(Integer leadId) {
        logger.info("Fetching documents for leadId: {}", leadId);

        List<DocumentsDTO> documents = repository.findByLeadsId(leadId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        logger.info("Found {} documents for leadId: {}", documents.size(), leadId);
        return documents;
    }

    private DocumentsDTO mapToDTO(Documents doc) {
        logger.debug("Mapping Documents entity to DTO: {}", doc);
        DocumentsDTO dto = new DocumentsDTO();
        dto.setDocumentId(doc.getDocumentId());
        dto.setLeadsId(doc.getLeadsId());
        dto.setDocumentType(doc.getDocumentType());
        dto.setFilePath(doc.getFilePath());
        dto.setUploadedAt(doc.getUploadedAt());
        return dto;
    }
}
