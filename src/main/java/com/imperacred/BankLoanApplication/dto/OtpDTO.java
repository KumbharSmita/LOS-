
package com.imperacred.BankLoanApplication.dto;

import com.imperacred.BankLoanApplication.model.Leads;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtpDTO {
    private Integer leadsId;
    private String message;

    public OtpDTO(Integer leadsId, String message) {
        this.leadsId = leadsId;
        this.message = message;
    }

    // Getters and Setters
}

