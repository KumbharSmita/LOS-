package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imperacred.BankLoanApplication.dto.AgentActivityLogDTO;
import com.imperacred.BankLoanApplication.service.AgentActivityLogService;

@RestController
@RequestMapping("/api/agent/activity")
public class AgentActivityLogController {
	 @Autowired
	    private AgentActivityLogService agentActivityLogService;

	    
	    @PostMapping("/log")
	    public ResponseEntity<AgentActivityLogDTO> logActivity(@RequestBody AgentActivityLogDTO dto) {
	        AgentActivityLogDTO loggedActivity = agentActivityLogService.logAgentActivity(dto);
	        return new ResponseEntity<>(loggedActivity, HttpStatus.CREATED);
	    }
	    
	    @GetMapping("/agentlog/{agent_id}")
	    public List<AgentActivityLogDTO> getLogByAgentId(@PathVariable("agent_id") int agent_id) {
	        return agentActivityLogService.getLogByAgentId(agent_id);
	    }
}
