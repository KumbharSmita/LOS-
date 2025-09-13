package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentActivityLogDTO {

    private int log_id;  
    private int agent_id;  
    private String actionType;  
    private String actionDetails;  
    private LocalDateTime actionTimestamp;  
   
   
}
