package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kyc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kycId;

    @OneToOne
    @JoinColumn(name = "leads_id")
    private Leads lead;

    @Column(name = "aadhar_number")
    private String aadharNumber;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "ekyc_status") // e.g., PENDING, SUCCESS, FAILED
    private String ekycStatus;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
}
