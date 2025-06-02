package com.imperacred.BankLoanApplication.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imperacred.BankLoanApplication.dto.DocumentsDTO;
import com.imperacred.BankLoanApplication.model.Documents;
import com.imperacred.BankLoanApplication.service.DocumentsService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentsController {

	private static final Logger logger = LogManager.getLogger(DocumentsController.class);

	private final DocumentsService documentsService;

	@PostMapping("/upload")
	public ResponseEntity<DocumentsDTO> uploadDocument(@RequestParam Integer leadsId, @RequestParam String documentType,
			@RequestParam MultipartFile file) {
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

	@GetMapping("/download/{documentId}")
	public ResponseEntity<Resource> downloadDocument(@PathVariable Integer documentId) throws IOException {
		logger.info(" Download request received for document ID: {}", documentId);

		Documents doc = documentsService.getDocumentById(documentId);
		File file = new File(doc.getFilePath());

		if (!file.exists()) {
			logger.error(" File not found on server for document ID: {} | Path: {}", documentId, doc.getFilePath());
			return ResponseEntity.notFound().build();
		}

		try {
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			String contentType = Files.probeContentType(file.toPath());
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			logger.info(" Serving document ID: {} | File: {} | Size: {} bytes | Content-Type: {}", documentId,
					file.getName(), file.length(), contentType);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
					.contentLength(file.length()).contentType(MediaType.parseMediaType(contentType)).body(resource);

		} catch (IOException e) {
			logger.error(" Error while streaming document ID: {}", documentId, e);
			throw e;
		}
	}

	@PostMapping("/request-reupload")
	public ResponseEntity<DocumentsDTO> requestReupload(@RequestParam Integer leadsId,
			@RequestParam String documentType) {
		logger.info("Received reupload request (by agent) for leadId: {}, documentType: {}", leadsId, documentType);
		try {
			DocumentsDTO result = documentsService.requestReupload(leadsId, documentType);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error("Failed to request reupload for leadId: {}, documentType: {} - {}", leadsId, documentType,
					e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/reupload-status/{leadId}")
	public ResponseEntity<Boolean> hasReupload(@PathVariable Integer leadId) {
		boolean needsReupload = documentsService.hasReuploadRequested(leadId);
		return ResponseEntity.ok(needsReupload);
	}

}
