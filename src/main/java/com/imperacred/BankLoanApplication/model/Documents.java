package com.imperacred.BankLoanApplication.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer document_id;

    @Column(name = "application_id", nullable = false)
    private Integer application_id;

    @Column(name = "document_type", nullable = false)
    private String document_type;

    @Column(name = "file_path", nullable = false)
    private String file_path;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploaded_at;
}