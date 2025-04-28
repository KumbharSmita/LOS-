//package com.imperacred.BankLoanApplication.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class KycDTO {
//    private Integer leadsId;
//    private String aadhaarNumber;
//    private String panNumber;
//}

package com.imperacred.BankLoanApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KycDTO {
    private Integer leadsId;
    private String aadharNumber;
    private String panNumber;
    private String ekycStatus; // e.g., "VERIFIED", "PENDING"
    private LocalDateTime verifiedAt;
}
