package com.imperacred.BankLoanApplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer leads_id;
    private String document_type;
    private String document_category;
    private String file_path;
    private LocalDateTime uploaded_at;
}
