package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadAssignmentResponseDTO {

	
    private Integer leads_id;
	private int agent_id;
	private LocalDateTime  assigned_at ;
	private String status;
	
}
