package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.GeneratorType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="leads")
public class Leads {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	 @Column(name = "leads_id") //  maps Java field to DB column
    private Integer leadId;     //  camelCase name used in code
	
	private String full_name;
	private String email;
	private String phone;
	private String source;
	private String status;
	
	//@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime created_at;// = LocalDateTime.now();



}

