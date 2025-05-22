package com.imperacred.BankLoanApplication.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadsDTO {
	
    private Integer leadsId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String panNumber;
	private String aadhaarNumber;
	private String source;
	private String loanType;
	private Double amount;
	private Integer tenureMonths;
	private String purpose;

}
