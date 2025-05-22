package com.imperacred.BankLoanApplication.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_registration")
public class UserRegistration implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_registration_id")
	private Integer userRegistrationId;
	@Column(name = "full_name")
	private String fullName;

	@Column(name = "contact_no") 
	private String contactNo;
	@Column(name="email")
	private String email; 
	@Column(name="password")
	private String password;

}
