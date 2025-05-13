package com.imperacred.BankLoanApplication.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.*;
=======
>>>>>>> origin/feature2
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoresDTO {
<<<<<<< HEAD

    @NotNull(message = "Leads ID is required") // Changed to @NotNull for primitive type
    private Integer leadsId;

    @NotBlank(message = "Bureau is required")
    private String bureau;

    @Min(value = 300, message = "Score must be >= 300")
    @Max(value = 900, message = "Score must be <= 900")
=======
	
>>>>>>> origin/feature2
    private int score;

    private String risk; // Optional in request; can be calculated

<<<<<<< HEAD
    private LocalDateTime fetchedAt; // Set in service layer
=======
>>>>>>> origin/feature2
}
