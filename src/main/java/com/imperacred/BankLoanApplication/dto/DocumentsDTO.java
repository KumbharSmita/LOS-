package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsDTO {
    private Integer document_id;
    private Integer leads_id;
    private String document_type;
    private String file_path;
    private LocalDateTime uploaded_at;
}