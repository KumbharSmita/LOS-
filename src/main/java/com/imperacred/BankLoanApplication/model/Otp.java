
package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer otpId;

    // Mapping Many-to-One relationship with Lead
    @ManyToOne
    @JoinColumn(name = "leads_id")
    private Lead lead;  // Instead of just leadsId, we use the full Lead object

    @Column(name = "otp_value", nullable = false)
    private String otpValue;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}



//public class Otp {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer otpId;
//
//    @ManyToOne
//    @JoinColumn(name = "leads_id", referencedColumnName = "leads_id", nullable = false)
//    private Leads lead;
//
//    @Column(name = "otp_value", nullable = false)
//    private String otpValue;
//
//    @Column(name = "expiry_time", nullable = false)
//    private LocalDateTime expiryTime;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//}
