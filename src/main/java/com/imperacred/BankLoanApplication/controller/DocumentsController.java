package com.imperacred.BankLoanApplication.controller;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.service.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Controller for uploading and downloading documents.
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentsController {

    @Autowired
    private DocumentsService documentsService;

    private static final String UPLOAD_DIR = "D:\\sources\\";

    /**
     * Uploads a document and saves metadata.
     */
    @PostMapping("/upload")
    public DocumentsDTO uploadDocument(
            @RequestParam("leads_id") Integer leadsId,
            @RequestParam("document_type") String documentType,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Ensure upload directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // Generate unique file name with extension
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // Save file to disk
        File destFile = new File(UPLOAD_DIR + filename);
        file.transferTo(destFile);

        // Build DTO and store metadata
        DocumentsDTO documentDTO = DocumentsDTO.builder()
                .leads_id(leadsId)
                .document_type(documentType)
                .file_path(destFile.getAbsolutePath())
                .uploaded_at(LocalDateTime.now())
                .build();

        return documentsService.createDocument(documentDTO);
    }

    /**
     * Downloads a document by ID.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer id) throws MalformedURLException {
        DocumentsDTO document = documentsService.getDocumentById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(document.getFile_path());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(file.toURI());

        // Dynamically detect content type (fallback to binary stream)
        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
}
