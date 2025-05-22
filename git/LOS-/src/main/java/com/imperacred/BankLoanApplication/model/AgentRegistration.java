package com.imperacred.BankLoanApplication.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="agent_registration")
public class AgentRegistration implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int agent_registration_id;//to-increment

    private String fullName;           // Agent's full name
    private String email;              // Agent's email
    private String password;
    private String contactno;         // Agent's contact number  
    private String office_location;   // Office location
    private String status;             // Status of the agent (e.g., active, inactive)
    @Enumerated(EnumType.STRING)       // Mapping ENUM to string
    @Column(nullable = false)          
    private Role role;                 // Agent's role

}
