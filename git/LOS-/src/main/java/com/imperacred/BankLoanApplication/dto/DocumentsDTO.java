package com.imperacred.BankLoanApplication.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentsDTO {
    private Integer documentId;
    private Integer leadsId;
    private String documentType;
    private String filePath;
    private LocalDateTime uploadedAt;
    private Boolean reuploadRequested;
}
