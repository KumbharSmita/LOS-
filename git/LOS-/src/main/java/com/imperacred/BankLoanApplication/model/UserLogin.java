
package com.imperacred.BankLoanApplication.model;

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
@Table(name="user_login")
public class UserLogin {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_login_id")
	private Integer userLoginId;
	@Column(name="user_registration_id")
	private Integer userRegistrationId;
	@Column(name="email")
	private String email;
	@Column(name="password")
	private String password;
	
	}
