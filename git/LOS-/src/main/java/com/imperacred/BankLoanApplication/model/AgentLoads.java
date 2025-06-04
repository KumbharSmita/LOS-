package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agent_loads")

public class AgentLoads {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int load_id;

	// Foreign key association to Agents entity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private Agents agent;

	@Column(name = "lead_count")
	private int leadCount;

	@Column(name = "last_assigned")
	private LocalDateTime lastAssigned;

}
