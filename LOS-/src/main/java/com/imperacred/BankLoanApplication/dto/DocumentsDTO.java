package com.imperacred.BankLoanApplication.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentsDTO {
    private Integer id;
    private Integer leads_id;
    private String document_type;
    private String document_category;
    private String file_path;
    private LocalDateTime uploaded_at;

    // Required for email service
    private String applicantName;
    private String email;
}
