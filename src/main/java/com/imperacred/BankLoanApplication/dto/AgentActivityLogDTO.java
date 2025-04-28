package com.imperacred.BankLoanApplication.dto;

import java.time.LocalDateTime;

public class AgentActivityLogDTO {

    private int log_id;  
    private int agent_id;  
    private String actionType;  
    private String actionDetails;  
    private LocalDateTime actionTimestamp;  
   
    public AgentActivityLogDTO() {
        super();
    }

    
    public AgentActivityLogDTO(int log_id, int agent_id, String actionType, String actionDetails, LocalDateTime actionTimestamp) {
        this.log_id = log_id;
        this.agent_id = agent_id;
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

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
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
