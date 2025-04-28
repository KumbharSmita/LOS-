package com.imperacred.BankLoanApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agent_activity_log")
public class AgentActivityLog {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int log_id;

	    @Column(name = "agent_id")
	    private int agentId;

	    @Column(name = "action_type")
	    private String actionType;

	    @Column(name = "action_details")
	    private String actionDetails;

	    @Column(name = "action_timestamp")
	    private LocalDateTime actionTimestamp;

		public AgentActivityLog() {
			super();
			// TODO Auto-generated constructor stub
		}

		public AgentActivityLog(int log_id, int agentId, String actionType, String actionDetails,
				LocalDateTime actionTimestamp) {
			super();
			this.log_id = log_id;
			this.agentId = agentId;
			this.actionType = actionType;
			this.actionDetails = actionDetails;
			this.actionTimestamp = actionTimestamp;
		}

		public int getLog_id() {
			return log_id;
		}

		public void setLog_id(int log_id) {
			this.log_id = log_id;
		}

		public int getAgentId() {
			return agentId;
		}

		public void setAgentId(int agentId) {
			this.agentId = agentId;
		}

		public String getActionType() {
			return actionType;
		}

		public void setActionType(String actionType) {
			this.actionType = actionType;
		}

		public String getActionDetails() {
			return actionDetails;
		}

		public void setActionDetails(String actionDetails) {
			this.actionDetails = actionDetails;
		}

		public LocalDateTime getActionTimestamp() {
			return actionTimestamp;
		}

		public void setActionTimestamp(LocalDateTime actionTimestamp) {
			this.actionTimestamp = actionTimestamp;
		}
	
}
