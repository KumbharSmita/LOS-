package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@Table(name = "agent_activity_log")
public class AgentActivityLog {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer log_id;

	    @Column(name = "agent_id")
	    private Integer agentId;

	    @Column(name = "action_type")
	    private String actionType;

	    @Column(name = "action_details")
	    private String actionDetails;

	    @Column(name = "action_timestamp")
	    private LocalDateTime actionTimestamp;

		
	
}
