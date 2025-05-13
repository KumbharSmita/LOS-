package com.imperacred.BankLoanApplication.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoresDTO {
	
    private int score;

    private String risk; // Optional in request; can be calculated

}
