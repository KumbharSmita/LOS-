package com.imperacred.BankLoanApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentLoginResponseDTO {
    private String token;
    private Integer agent_id;
    private String email;
}
