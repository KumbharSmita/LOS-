package com.imperacred.BankLoanApplication.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.Email;
=======
>>>>>>> origin/feature2
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadsDTO {
<<<<<<< HEAD

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

=======
	


	    private String firstName;
	    private String lastName;
	    private String email;
	    private String phone;
	    private String panNumber;
	    private String aadhaarNumber;
	    private String source;
	    private Integer credit_score;
	    private String loanType;
	    private Double amount;
	    private Integer tenureMonths;
	    private String purpose;
>>>>>>> origin/feature2
}

//    @NotEmpty
//    private String firstName;
//
//    @NotEmpty
//    private String lastName;
//
//    @Email
//    @NotEmpty
//    private String email;
//
//    @NotEmpty
//    private String phone;
//
//    @NotEmpty
//    private String panNumber;
//
//    @NotEmpty
//    private String aadhaarNumber;
//
//    private String source;
//    private String loanType;
//    private Double amount;
//    private Integer tenureMonths;
//    private String purpose;
//
//}
