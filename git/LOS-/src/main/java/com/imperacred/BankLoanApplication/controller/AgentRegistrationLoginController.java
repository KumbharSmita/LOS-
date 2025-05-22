package com.imperacred.BankLoanApplication.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.imperacred.BankLoanApplication.dto.AgentLoginDTO;
import com.imperacred.BankLoanApplication.dto.AgentLoginResponseDTO;
import com.imperacred.BankLoanApplication.dto.AgentRegistrationDTO;
import com.imperacred.BankLoanApplication.model.AgentRegistration;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.service.AgentRegistrationLoginService;
import com.imperacred.BankLoanApplication.service.AgentsService;
import com.imperacred.BankLoanApplication.util.JwtUtil;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/agent")
public class AgentRegistrationLoginController {

    private static final Logger logger = LogManager.getLogger(AgentRegistrationLoginController.class);

    @Autowired
    private AgentRegistrationLoginService agentRegistrationLoginService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AgentRegistration> registerAgent(@RequestBody AgentRegistrationDTO dto) {
        logger.info("Received agent registration request for email: {}", dto.getEmail());

        try {
            AgentRegistration registeredAgent = agentRegistrationLoginService.registerAgent(dto);
            logger.info("Agent registered successfully for email: {}", dto.getEmail());
            return new ResponseEntity<>(registeredAgent, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Agent registration failed for email: {}", dto.getEmail(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
  

    @PostMapping("/login")
    public ResponseEntity<AgentLoginResponseDTO> loginAgent(@RequestBody AgentLoginDTO dto) {
        try {
          
            AgentLoginResponseDTO response = agentRegistrationLoginService.loginAgentAndGetToken(dto);

            // Return the response DTO with the JWT token and agent details
            return ResponseEntity.ok(response);  // HTTP 200 OK
        } catch (RuntimeException e) {
            // In case of authentication failure, return an Unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(new AgentLoginResponseDTO("Error: Invalid credentials", null, null));
        }
    }






    @GetMapping("/{agents_id}")
    public Agents getAgentById(@PathVariable int agents_id) {
        logger.info("Fetching agent by ID: {}", agents_id);
        Agents agent = agentRegistrationLoginService.getAgentById(agents_id);
        if (agent == null) {
            logger.warn("No agent found for ID: {}", agents_id);
        } else {
            logger.debug("Agent found: {}", agent.getEmail());
        }
        return agent;
    }

    @GetMapping
    public List<Agents> getAllAgents() {
        logger.info("Fetching all agents");
        return agentRegistrationLoginService.getAllAgents();
    }

    @PutMapping("/{agents_id}")
    public Agents updateAgent(@PathVariable int agents_id, @RequestBody AgentRegistrationDTO agentDTO) {
        logger.info("Updating agent with ID: {}", agents_id);
        return agentRegistrationLoginService.updateAgent(agents_id, agentDTO);
    }

    @DeleteMapping("/{agents_id}")
    public ResponseEntity<String> deleteAgent(@PathVariable int agents_id) {
        logger.info("Deleting agent with ID: {}", agents_id);
        String result = agentRegistrationLoginService.deleteAgent(agents_id);

        if ("Agent deleted successfully".equals(result)) {
            logger.info("Agent deleted: {}", agents_id);
            return ResponseEntity.ok(result);
        } else {
            logger.warn("Failed to delete agent: {}", agents_id);
            return ResponseEntity.status(404).body(result);
        }
    }
}