package com.imperacred.BankLoanApplication.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
	private Integer user_registration_id;
	private String email;
    private String password;
	
}
