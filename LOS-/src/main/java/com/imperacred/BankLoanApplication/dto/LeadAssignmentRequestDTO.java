package com.imperacred.BankLoanApplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadAssignmentRequestDTO {

    @JsonProperty("leads_id")
    private Integer leadsId;

	

}
