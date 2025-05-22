package com.imperacred.BankLoanApplication.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
	private Integer userRegistrationId;
	private String email;
    private String password;
	
}