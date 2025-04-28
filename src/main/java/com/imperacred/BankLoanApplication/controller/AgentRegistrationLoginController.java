package com.imperacred.BankLoanApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imperacred.BankLoanApplication.dto.AgentLoginDTO;
import com.imperacred.BankLoanApplication.dto.AgentRegistrationDTO;
import com.imperacred.BankLoanApplication.model.AgentRegistration;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.service.AgentRegistrationLoginService;

@RestController
@RequestMapping("/api/agent")
public class AgentRegistrationLoginController {
@Autowired
private AgentRegistrationLoginService agentRegistrationLoginService;

@PostMapping("/register")
public ResponseEntity<AgentRegistration> registerAgent(@RequestBody AgentRegistrationDTO dto) {
    AgentRegistration registeredAgent = agentRegistrationLoginService.registerAgent(dto);
    return new ResponseEntity<>(registeredAgent, HttpStatus.CREATED);
}


@PostMapping("/login")
public ResponseEntity<String> loginAgent(@RequestBody AgentLoginDTO dto) {
    boolean isLoggedIn = agentRegistrationLoginService.loginAgent(dto);
    if (isLoggedIn) {
        return ResponseEntity.ok("Login successful");
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}


@GetMapping("/{agents_id}")
public Agents getAgentById(@PathVariable int agents_id) {
    return agentRegistrationLoginService.getAgentById(agents_id); 
}
@GetMapping
public List<Agents> getAllAgents() {
    return agentRegistrationLoginService.getAllAgents();  
}

@PutMapping("/{agents_id}")
public Agents updateAgent(@PathVariable int agents_id, @RequestBody AgentRegistrationDTO agentDTO) {
    return agentRegistrationLoginService.updateAgent(agents_id, agentDTO);
}

@DeleteMapping("/{agents_id}")
public ResponseEntity<String> deleteAgent(@PathVariable int agents_id) {
    String result = agentRegistrationLoginService.deleteAgent(agents_id);

    if ("Agent deleted successfully".equals(result)) {
        return ResponseEntity.ok(result);
    } else {
        return ResponseEntity.status(404).body(result); 
    }
}


}
