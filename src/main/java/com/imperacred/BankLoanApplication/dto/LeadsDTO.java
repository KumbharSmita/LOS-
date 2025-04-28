package com.imperacred.BankLoanApplication.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadsDTO {
    
    private Integer leads_id;
    private String full_name;
    private String email;
    private String phone;
    private String source;
    private String status;
    private LocalDateTime createdAt;

  
}
