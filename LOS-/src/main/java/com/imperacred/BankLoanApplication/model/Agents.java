package com.imperacred.BankLoanApplication.model;

import jakarta.persistence.Entity;
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
@Table(name="agents")
public class Agents {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int agent_id;
	private int agent_registration_id;
	private String full_name;
	private String email;
	private String contactno;
	private String office_location;
	private String status;
	
	

}
