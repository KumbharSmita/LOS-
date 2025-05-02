package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.AgentActivityLogDTO;
import com.imperacred.BankLoanApplication.service.AgentActivityLogService;

@RestController
@RequestMapping("/api/agent/activity")
public class AgentActivityLogController {

    private static final Logger logger = LogManager.getLogger(AgentActivityLogController.class);

    @Autowired
    private AgentActivityLogService agentActivityLogService;

    // Log activity for agent
    @PostMapping("/log")
    public ResponseEntity<AgentActivityLogDTO> logActivity(@RequestBody AgentActivityLogDTO dto) {
        logger.info("Received request to log activity: Agent ID = {}, Action Type = {}, Action Details = {}",
                dto.getAgent_id(), dto.getActionType(), dto.getActionDetails());

        // Calling the service to log the agent activity
        AgentActivityLogDTO loggedActivity = agentActivityLogService.logAgentActivity(dto);

        logger.info("Activity logged successfully for Agent ID = {} with log ID = {}", 
                dto.getAgent_id(), loggedActivity.getLog_id());

        return new ResponseEntity<>(loggedActivity, HttpStatus.CREATED);  // Return created status
    }

    // Fetch activity logs by Agent ID
    @GetMapping("/agentlog/{agent_id}")
    public ResponseEntity<List<AgentActivityLogDTO>> getLogByAgentId(@PathVariable("agent_id") int agent_id) {
        logger.info("Fetching activity logs for Agent ID = {}", agent_id);

        // Fetch logs for the agent using the service
        List<AgentActivityLogDTO> logs = agentActivityLogService.getLogByAgentId(agent_id);

        logger.info("Found {} log(s) for Agent ID = {}", logs.size(), agent_id);

        return new ResponseEntity<>(logs, HttpStatus.OK);  // Return logs with OK status
    }
}
