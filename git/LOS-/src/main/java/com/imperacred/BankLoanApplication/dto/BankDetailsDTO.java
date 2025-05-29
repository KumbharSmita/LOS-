package com.imperacred.BankLoanApplication.dto;

import com.imperacred.BankLoanApplication.model.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankDetailsDTO {
	  private Integer leadsId;

	    @NotBlank(message = "Account holder name is required")
	    private String accountHolderName;

	    @NotBlank(message = "Account number is required")
	    @Size(min = 6, max = 50)
	    private String accountNumber;

	    @NotBlank(message = "IFSC code is required")
	    @Size(min = 11, max = 11)
	    private String ifscCode;
}
