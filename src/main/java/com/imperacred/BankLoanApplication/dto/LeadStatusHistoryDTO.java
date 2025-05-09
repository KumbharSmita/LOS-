package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadStatusHistoryDTO {

    private int history_id;  
    private Integer leads_id;   
    private int agent_id;    
    private String status;
    private LocalDateTime updated_at;
	
   
}
