package com.imperacred.BankLoanApplication.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private Integer otpId;

    @ManyToOne
    @JoinColumn(name = "leads_id", nullable = false, unique = true)
    private Leads leads;

    @Column(name = "otp_value", length = 6)
    private String otpValue;

    @Column(name = "expiry_time")
    private Timestamp expiryTime;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}

