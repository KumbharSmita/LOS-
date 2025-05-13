//package com.imperacred.BankLoanApplication.model;
//
<<<<<<< HEAD
//import jakarta.persistence.*;
=======
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
>>>>>>> origin/feature2
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
<<<<<<< HEAD
//import java.time.LocalDateTime;
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "kyc")
=======
//@Entity
//@Table(name = "kyc")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
>>>>>>> origin/feature2
//public class Kyc {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer kycId;
//
<<<<<<< HEAD
//    @ManyToOne
//    @JoinColumn(name = "leads_id")
//    private Leads lead;
//
//    private String aadharNumber;
//    private String panNumber;
//    private String ekycStatus;
//
//    private LocalDateTime verifiedAt;
//
//  
=======
//    @OneToOne
//    @JoinColumn(name = "leads_id")
//    private Leads lead;
//
//    @Column(name = "aadhar_number")
//    private String aadharNumber;
//
//    @Column(name = "pan_number")
//    private String panNumber;
//
//    @Column(name = "ekyc_status") // e.g., PENDING, SUCCESS, FAILED
//    private String ekycStatus;
//
//    @Column(name = "verified_at")
//    private LocalDateTime verifiedAt;
>>>>>>> origin/feature2
//}
