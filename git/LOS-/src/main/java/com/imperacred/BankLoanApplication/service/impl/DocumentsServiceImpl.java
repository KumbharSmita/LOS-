package com.imperacred.BankLoanApplication.service.impl;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.dto.LeadAssignmentResponseDTO;
import com.imperacred.BankLoanApplication.model.Documents;
import com.imperacred.BankLoanApplication.model.Lead;
import com.imperacred.BankLoanApplication.repository.DocumentsRepository;
import com.imperacred.BankLoanApplication.repository.LeadsRepository;
import com.imperacred.BankLoanApplication.service.DocumentsService;
import com.imperacred.BankLoanApplication.service.EmailService;
import com.imperacred.BankLoanApplication.service.LeadAssignmentService;

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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentsServiceImpl implements DocumentsService {

    private static final Logger logger = LogManager.getLogger(DocumentsServiceImpl.class);

    private final DocumentsRepository documentsRepository;
    private final LeadsRepository leadsRepository;
    private final LeadAssignmentService leadAssignmentService;
    private final EmailService emailService;

    @Override
    public DocumentsDTO uploadDocument(Integer leadId, String documentType, MultipartFile file) {
        logger.info("Uploading document for leadId: {}, documentType: {}", leadId, documentType);

        try {
            String userHome = System.getProperty("user.home");
            String desktopUploadsDir = userHome + File.separator + "Desktop" + File.separator + "uploads";
            Files.createDirectories(Paths.get(desktopUploadsDir));

            String fileName = leadId + "_" + documentType + "_" + file.getOriginalFilename();
            String filePath = desktopUploadsDir + File.separator + fileName;
            file.transferTo(new File(filePath));

           
            Optional<Documents> existingDocOpt = documentsRepository.findByLeadsIdAndDocumentType(leadId, documentType);
            existingDocOpt.ifPresent(existingDoc -> {
                if (Boolean.TRUE.equals(existingDoc.getReuploadRequested())) {
                    logger.info("Resetting reuploadRequested flag for document ID: {}", existingDoc.getDocumentId());
                    existingDoc.setReuploadRequested(false);
                    documentsRepository.save(existingDoc);
                }
            });

            // Save new document
            Documents doc = new Documents();
            doc.setLeadsId(leadId);
            doc.setDocumentType(documentType);
            doc.setFilePath(filePath);
            doc.setUploadedAt(LocalDateTime.now());
            doc.setReuploadRequested(false);

            Documents savedDoc = documentsRepository.save(doc);
            logger.info("Document saved successfully. ID: {}, Path: {}", savedDoc.getDocumentId(), filePath);

            maybeAssignLeadAfterDocumentUpload(leadId);

            return mapToDTO(savedDoc);

        } catch (IOException e) {
            logger.error("Failed to upload file for leadId: {}", leadId, e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private void maybeAssignLeadAfterDocumentUpload(Integer leadId) {
        Lead lead = leadsRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found with ID: " + leadId));

        if (!"OTP VERIFIED".equalsIgnoreCase(lead.getStatus()) || lead.getCreditScore() == null) {
            logger.info("Lead ID {} not eligible for assignment yet (status: {}, creditScore: {})",
                    leadId, lead.getStatus(), lead.getCreditScore());
            return;
        }

        List<String> uploadedDocs = documentsRepository.findByLeadsId(leadId).stream()
                .map(Documents::getDocumentType)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        boolean hasSalarySlip = uploadedDocs.contains("salary slip");
        boolean hasBankStatement = uploadedDocs.contains("bank statement");

        if (hasSalarySlip && hasBankStatement) {
            logger.info("All required documents uploaded for leadId: {}. Assigning agent...", leadId);

            if ("LEAD ASSIGNED".equalsIgnoreCase(lead.getStatus())) {
                logger.info("Lead ID {} is already assigned. Skipping reassignment.", leadId);
                return;
            }

            LeadAssignmentResponseDTO assignment = leadAssignmentService.assignLeadToAgent(leadId);

            lead.setStatus("LEAD ASSIGNED");
            leadsRepository.save(lead);

            LocalDateTime expectedContactTime = assignment.getAssigned_at().plusHours(2);
            String body = String.format(
                    "Dear %s,\n\nYour loan application (Lead ID: %d) has been assigned to Agent ID: %d.\n" +
                            "You can expect a call or email by: %s.\n\nThanks,\nImperaCred Team",
                    lead.getFirstName(), leadId, assignment.getAgent_id(), expectedContactTime
            );
            emailService.sendSimpleEmail(lead.getEmail(), "Agent Assigned", body);

            logger.info("Email notification sent to {} for agent assignment.", lead.getEmail());

        } else {
            logger.info("Documents incomplete for leadId: {}. Awaiting both Salary Slip and Bank Statement.", leadId);
        }
    }

    @Override
    public List<DocumentsDTO> getDocumentsByLeadId(Integer leadId) {
        logger.info("Fetching documents list for leadId: {}", leadId);
        List<DocumentsDTO> docs = documentsRepository.findByLeadsId(leadId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        logger.info("Found {} documents for leadId: {}", docs.size(), leadId);
        return docs;
    }

    @Override
    public Documents getDocumentById(Integer documentId) {
        logger.info("Looking up document by ID: {}", documentId);
        return documentsRepository.findById(documentId)
                .map(doc -> {
                    logger.info("Document found. ID: {}, Path: {}", doc.getDocumentId(), doc.getFilePath());
                    return doc;
                })
                .orElseThrow(() -> {
                    logger.error("Document not found for ID: {}", documentId);
                    return new RuntimeException("Document not found with ID: " + documentId);
                });
    }

    private DocumentsDTO mapToDTO(Documents doc) {
        DocumentsDTO dto = new DocumentsDTO();
        dto.setDocumentId(doc.getDocumentId());
        dto.setLeadsId(doc.getLeadsId());
        dto.setDocumentType(doc.getDocumentType());
        dto.setFilePath(doc.getFilePath());
        dto.setUploadedAt(doc.getUploadedAt());
        dto.setReuploadRequested(doc.getReuploadRequested());
        return dto;
    }

    @Override
    public DocumentsDTO requestReupload(Integer leadId, String documentType) {
        logger.info("Agent requesting reupload for leadId: {}, documentType: {}", leadId, documentType);

        Documents doc = documentsRepository.findByLeadsIdAndDocumentType(leadId, documentType)
                .orElseThrow(() -> new RuntimeException("Document not found for reupload request"));

        doc.setReuploadRequested(true);
        Documents updatedDoc = documentsRepository.save(doc);

        leadsRepository.findById(leadId).ifPresent(lead -> {
            String body = String.format(
                    "Dear %s,\n\nYour %s document was found to be incorrect or missing.\n" +
                            "Please reupload the document at your earliest convenience.\n\nThanks,\nImperaCred Team",
                    lead.getFirstName(), documentType);
            emailService.sendSimpleEmail(lead.getEmail(), "Document Reupload Requested", body);
        });

        logger.info("Reupload request updated and notification sent");
        return mapToDTO(updatedDoc);
    }

    @Override
    public boolean hasReuploadRequested(Integer leadsId) {
        List<Documents> documents = documentsRepository.findByLeadsId(leadsId);
        return documents.stream().anyMatch(doc -> Boolean.TRUE.equals(doc.getReuploadRequested()));
    }
}
