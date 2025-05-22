package com.imperacred.BankLoanApplication.model;

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
@Table(name="agent_login")
public class AgentLogin {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer agent_login_id;
	private int agent_registration_id;
	private String email;
	private String password;
	
	
}
