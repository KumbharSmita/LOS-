package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer documentId;

    @Column(nullable = false)
    private Integer leadsId;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;
    
    private Boolean reuploadRequested = false;
}
