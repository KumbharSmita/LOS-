package com.imperacred.BankLoanApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leads")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leads_id")
    private Integer leadsId;

    @Column(name = "displayid")
    private String displayid;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "aadhaar_number")
    private String aadhaarNumber;

    @Column(name = "source")
    private String source;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "tenure_months")
    private Integer tenureMonths;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @Column(name="credit_score")
    private Integer creditScore; 
    
    @Column(name="confirmed_amount")
    private BigDecimal confirmedAmount;
    
    @Column(name="confirmed_tenure_months")
    private Integer confirmedTenureMonths;
    
    @Column(name = "disbursement_otp_status")
    private String disbursementOtpStatus;
    
    
    @Column(name = "bank_account_holder_name")
    private String bankAccountHolderName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_ifsc_code")
    private String bankIfscCode;


    // PrePersist annotation to set createdAt before saving a new entity
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
    }

    // PreUpdate annotation to update timestamps before updating an existing entity
    @PreUpdate
    public void preUpdate() {
        this.submittedAt = LocalDateTime.now();
    }
}



//@Table(name = "`lead`")
//
//public class Leads {
//
//	@Id
//	@Column(name = "leads_id", nullable = false, unique = true)
//	private String leadsId;
//
//
//   
//
//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "phone")
//    private String phone;
//
//    @Column(name = "pan_number")
//    private String panNumber;
//
//    @Column(name = "aadhaar_number")
//    private String aadhaarNumber;
//
//    private String source;
//    private String loanType;
//    private Double amount;
//    private Integer tenureMonths;
//    private String purpose;
//    private String status;
//
//    private LocalDateTime createdAt;
//    private LocalDateTime submittedAt;
//
//  
//
//    
//}

	

   



