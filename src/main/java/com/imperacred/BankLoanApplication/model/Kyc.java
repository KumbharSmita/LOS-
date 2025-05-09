//package com.imperacred.BankLoanApplication.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "kyc")
//public class Kyc {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer kycId;
//
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
//}
