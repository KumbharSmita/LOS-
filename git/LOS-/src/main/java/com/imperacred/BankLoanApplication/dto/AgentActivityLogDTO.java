package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentActivityLogDTO {

    private Integer log_id;  
    private Integer agent_id;  
    private String actionType;  
    private String actionDetails;  
    private LocalDateTime actionTimestamp;  
   
   
}
