package com.imperacred.BankLoanApplication.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.AgentLoginDTO;
import com.imperacred.BankLoanApplication.dto.AgentRegistrationDTO;
import com.imperacred.BankLoanApplication.model.AgentLoads;
import com.imperacred.BankLoanApplication.model.AgentLogin;
import com.imperacred.BankLoanApplication.model.AgentRegistration;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.repository.AgentLoadsRepository;
import com.imperacred.BankLoanApplication.repository.AgentLoginRepository;
import com.imperacred.BankLoanApplication.repository.AgentRegistrationRepository;
import com.imperacred.BankLoanApplication.repository.AgentsRepository;
import com.imperacred.BankLoanApplication.service.AgentRegistrationLoginService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AgentRegistrationLoginServiceImpl implements AgentRegistrationLoginService {

	private static final Logger logger = LogManager.getLogger(AgentRegistrationLoginServiceImpl.class);

	
    @Autowired
    private AgentRegistrationRepository agentRegistrationRepository;

    @Autowired
    private AgentLoginRepository agentLoginRepository;

    @Autowired
    private AgentsRepository agentsRepository;

    @Autowired
    private AgentLoadsRepository agentLoadRepository;

    @Override
    public AgentRegistration registerAgent(AgentRegistrationDTO dto) {
    	
    	 logger.info("Starting agent registration for email: {}", dto.getEmail());

    	 if (agentRegistrationRepository.existsByEmail(dto.getEmail())) {
    	        throw new RuntimeException("Email is already registered.");
    	    }

    	    // Check if contact number already exists
    	    if (agentRegistrationRepository.existsByContactno(dto.getContactno())) {
    	        throw new RuntimeException("Contact number is already registered.");
    	    }
        // Register agent in the agent_registration table
        AgentRegistration agent = new AgentRegistration();
        agent.setFullName(dto.getFull_name());
        agent.setEmail(dto.getEmail());
        agent.setPassword(dto.getPassword());
        agent.setContactno(dto.getContactno());
        agent.setOffice_location(dto.getOffice_location());
        agent.setStatus(dto.getStatus());
        AgentRegistration savedAgent = agentRegistrationRepository.save(agent);
        
        logger.debug("Saved agent registration: {}", savedAgent);

        // Register agent login details in the agent_login table
        AgentLogin login = new AgentLogin();
        login.setAgent_registration_id(savedAgent.getAgent_registration_id());
        login.setEmail(savedAgent.getEmail());
        login.setPassword(savedAgent.getPassword());
        agentLoginRepository.save(login);

        logger.debug("Saved agent login details for: {}", login.getEmail());
        //  Save agent details in the agents table
        Agents basicAgent = new Agents();
        basicAgent.setAgent_registration_id(savedAgent.getAgent_registration_id());
        basicAgent.setFull_name(savedAgent.getFullName());
        basicAgent.setEmail(savedAgent.getEmail());
        basicAgent.setContactno(savedAgent.getContactno());
        basicAgent.setOffice_location(savedAgent.getOffice_location());
        basicAgent.setStatus(savedAgent.getStatus());
        basicAgent = agentsRepository.save(basicAgent);
        
        logger.debug("Saved basic agent info: {}", basicAgent.getEmail());


        //  Create entry in agent_loads table
        AgentLoads agentLoad = new AgentLoads();
        agentLoad.setAgent_id(basicAgent.getAgent_id());  // Assuming basicAgent has the getAgent_id method
        agentLoad.setLeadCount(0); // Initial lead count set to 0
        agentLoad.setLastAssigned(null); // No assignment yet, set to null (or LocalDateTime.now() if desired)
        agentLoadRepository.save(agentLoad);
        return savedAgent;

    }

    @Override
    public boolean loginAgent(AgentLoginDTO dto) {
        return agentLoginRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword()).isPresent();
    }

    @Override
    public Agents getAgentById(int agents_id) {
        Optional<Agents> agent = agentsRepository.findById(agents_id);
        return agent.orElse(null);
    }

    @Override
    public List<Agents> getAllAgents() {
        return agentsRepository.findAll();
    }

    @Override
    public Agents updateAgent(int agents_id, AgentRegistrationDTO agentDTO) {
        Optional<Agents> existingAgent = agentsRepository.findById(agents_id);

        if (existingAgent.isPresent()) {
            Agents agent = existingAgent.get();
            agent.setFull_name(agentDTO.getFull_name());
            agent.setEmail(agentDTO.getEmail());
            agent.setContactno(agentDTO.getContactno());
            agent.setOffice_location(agentDTO.getOffice_location());
            agent.setStatus(agentDTO.getStatus());

            return agentsRepository.save(agent);
        } else {
            System.out.println("Agent not found");
        }
        return null;
    }

    @Override
    public String deleteAgent(int agents_id) {
        return agentsRepository.findById(agents_id)
            .map(agent -> {
                agentsRepository.delete(agent);
                return "Agent deleted successfully";
            })
            .orElse("Agent not found");
    }
}
