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

import jakarta.servlet.http.HttpServletRequest;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/agent/activity")
public class AgentActivityLogController {

    private static final Logger logger = LogManager.getLogger(AgentActivityLogController.class);

    @Autowired
    private AgentActivityLogService agentActivityLogService;

    // Log activity for agent
    @PostMapping("/log")
    public ResponseEntity<AgentActivityLogDTO> logActivity(
            @RequestBody AgentActivityLogDTO dto,
            HttpServletRequest request) {
        logger.info("Received request to log activity: Agent ID = {}, Action Type = {}, Action Details = {}",
                dto.getAgent_id(), dto.getActionType(), dto.getActionDetails());

        AgentActivityLogDTO savedLog = agentActivityLogService.logAgentActivity(dto, request);

        logger.info("Activity logged successfully for Agent ID = {} with log ID = {}", 
                    dto.getAgent_id(), savedLog.getLog_id());

        return ResponseEntity.ok(savedLog);
    }

    // Fetch activity logs by Agent ID
    @GetMapping("/agentlog/{agent_id}")
    public ResponseEntity<List<AgentActivityLogDTO>> getLogByAgentId(@PathVariable("agent_id") Integer agent_id, HttpServletRequest request) {
        logger.info("Fetching activity logs for Agent ID = {}", agent_id);

        // Fetch the logs from the service layer
        List<AgentActivityLogDTO> logs = agentActivityLogService.getLogByAgentId(agent_id, request);

        logger.info("Found {} log(s) for Agent ID = {}", logs.size(), agent_id);

        return new ResponseEntity<>(logs, HttpStatus.OK);  // Return logs with OK status
    }
 
    @GetMapping("/all-logs")
    public ResponseEntity<List<AgentActivityLogDTO>> getAllLogs(
            @RequestParam(required = false) Integer agent_id,
            HttpServletRequest request) {

        logger.info("Request to fetch {} activity logs",
                agent_id != null ? "logs for agent_id=" + agent_id : "all");

        List<AgentActivityLogDTO> logs = agentActivityLogService.getAllLogs(agent_id, request);
        logger.info("Fetched {} log(s)", logs.size());

        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

}
