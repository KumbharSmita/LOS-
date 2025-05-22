package com.imperacred.BankLoanApplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    @JsonProperty("user_registration_id")
    private Integer userRegistrationId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("contact_no")
    private String contactNo;

    private String email;

    private String password;
}
