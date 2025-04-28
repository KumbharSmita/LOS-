package com.imperacred.BankLoanApplication.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
	public AgentRegistration() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AgentRegistration(int agent_registration_id, String fullName, String email,  String password ,String contactno,
			String office_location, String status) {
		super();
		this.agent_registration_id = agent_registration_id;
		this.fullName = fullName;
		this.email = email;
		this.password=password;
		this.contactno = contactno;
		this.office_location = office_location;
		this.status = status;
	}
	public int getAgent_registration_id() {
		return agent_registration_id;
	}
	public void setAgent_registration_id(int agent_registration_id) {
		this.agent_registration_id = agent_registration_id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	 public String getPassword() {
		return password;
		 
	 }
	 public void setPassword(String password) {
		 this.password=password;
	 }
	
	public String getContactno() {
		return contactno;
	}
	public void setContactno(String contactno) {
		this.contactno = contactno;
	}
	public String getOffice_location() {
		return office_location;
	}
	public void setOffice_location(String office_location) {
		this.office_location = office_location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
