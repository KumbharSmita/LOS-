package com.imperacred.BankLoanApplication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.dto.AgentLoginDTO;
import com.imperacred.BankLoanApplication.dto.AgentLoginResponseDTO;
import com.imperacred.BankLoanApplication.dto.AgentRegistrationDTO;
import com.imperacred.BankLoanApplication.model.AgentLoads;
import com.imperacred.BankLoanApplication.model.AgentLogin;
import com.imperacred.BankLoanApplication.model.AgentRegistration;
import com.imperacred.BankLoanApplication.model.Agents;
import com.imperacred.BankLoanApplication.model.Role;
import com.imperacred.BankLoanApplication.repository.AgentLoadsRepository;
import com.imperacred.BankLoanApplication.repository.AgentLoginRepository;
import com.imperacred.BankLoanApplication.repository.AgentRegistrationRepository;
import com.imperacred.BankLoanApplication.repository.AgentsRepository;
import com.imperacred.BankLoanApplication.service.AgentRegistrationLoginService;
import com.imperacred.BankLoanApplication.util.ValidationUtil;
import com.imperacred.BankLoanApplication.util.JwtUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AgentRegistration registerAgent(AgentRegistrationDTO dto) {
        logger.info("Starting agent registration for email: {}", dto.getEmail());

        // Log the user's input values for tracking (Be mindful of not logging sensitive information such as passwords)
        logger.debug("User input - Full Name: {}, Email: {}, Contact Number: {}", dto.getFull_name(), dto.getEmail(), dto.getContactno());

        // Validate Email Format
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            logger.error("Invalid email format for: {}", dto.getEmail());
            throw new RuntimeException("Invalid email format.");
        }

        // Validate Contact Number Format (should be 10 digits)
        if (!ValidationUtil.isValidPhoneNumber(dto.getContactno())) {
            logger.error("Invalid contact number for: {}", dto.getContactno());
            throw new RuntimeException("Invalid contact number. It should be 10 digits.");
        }

        // Check if email is already registered
        if (agentRegistrationRepository.existsByEmail(dto.getEmail())) {
            logger.error("Email already registered: {}", dto.getEmail());
            throw new RuntimeException("Email is already registered.");
        }

        // Check if contact number is already registered
        if (agentRegistrationRepository.existsByContactno(dto.getContactno())) {
            logger.error("Contact number already registered: {}", dto.getContactno());
            throw new RuntimeException("Contact number is already registered.");
        }

        // Register agent in the agent_registration table without password hashing
        AgentRegistration agent = new AgentRegistration();
        agent.setFullName(dto.getFull_name());
        agent.setEmail(dto.getEmail());
        agent.setPassword(dto.getPassword());  // Storing password as-is without hashing
        agent.setContactno(dto.getContactno());
        agent.setOffice_location(dto.getOffice_location());
        agent.setStatus(dto.getStatus());
        agent.setRole(dto.getRole());
        AgentRegistration savedAgent = agentRegistrationRepository.save(agent);

        logger.info("Agent registration successful for: {}", dto.getEmail());

        // Register agent login details in the agent_login table
        AgentLogin login = new AgentLogin();
        login.setAgent_registration_id(savedAgent.getAgent_registration_id());
        login.setEmail(savedAgent.getEmail());
        login.setPassword(savedAgent.getPassword());
        agentLoginRepository.save(login);

        logger.debug("Saved agent login details for: {}", login.getEmail());

        // Save agent details in the agents table
        Agents basicAgent = new Agents();
        basicAgent.setAgentRegistrationId(savedAgent.getAgent_registration_id());
        basicAgent.setFull_name(savedAgent.getFullName());
        basicAgent.setEmail(savedAgent.getEmail());
        basicAgent.setContactno(savedAgent.getContactno());
        basicAgent.setOffice_location(savedAgent.getOffice_location());
        basicAgent.setStatus(savedAgent.getStatus());
        basicAgent.setRole(savedAgent.getRole());
        basicAgent = agentsRepository.save(basicAgent);

        logger.info("Saved basic agent info for: {}", basicAgent.getEmail());

        // Create entry in agent_loads table
        AgentLoads agentLoad = new AgentLoads();
        agentLoad.setAgent_id(basicAgent.getAgent_id());  // Assuming basicAgent has the getAgent_id method
        agentLoad.setLeadCount(0); // Initial lead count set to 0
        agentLoad.setLastAssigned(null); // No assignment yet, set to null (or LocalDateTime.now() if desired)
        agentLoadRepository.save(agentLoad);

        logger.debug("Created agent load entry for: {}", basicAgent.getEmail());

        return savedAgent;
    }
    @Override
    public AgentLoginResponseDTO loginAgentAndGetToken(AgentLoginDTO dto) {
        logger.info("Attempting login for email: {}", dto.getEmail());

        // Step 1: Verify the agent's credentials
        Optional<AgentRegistration> agentRegistration = agentRegistrationRepository.findByEmail(dto.getEmail());

        if (agentRegistration.isPresent()) {
            logger.info("Agent found in agent_registration table.");

            // Step 2: Check if the password matches the one stored
            if (agentRegistration.get().getPassword().equals(dto.getPassword())) {
                logger.info("Password matched, proceeding to generate token.");

                // Step 3: Retrieve agent details from the agents table
                Optional<Agents> agent = agentsRepository.findByAgentRegistrationId(agentRegistration.get().getAgent_registration_id());

                if (agent.isPresent()) {
                    logger.info("Agent found in agents table.");

                    // Extract the email and role from agentRegistration
                    String email = agentRegistration.get().getEmail();
                    String roleString = agentRegistration.get().getRole().name();  // Extract role as a string
                    Role role = Role.valueOf(roleString);  // Convert role string to Role enum

                    // Step 4: Generate JWT token using email and role
                    try {
                        String token = jwtUtil.generateToken(email, role);  // Generate the token
                        logger.info("Generated JWT Token for {}: {}", email, token);
                        // Step 5: Create and return the response DTO
                        return new AgentLoginResponseDTO(token, agent.get().getAgent_id(), email);
                    } catch (Exception e) {
                        logger.error("Error generating JWT token", e);
                        throw new RuntimeException("Error generating JWT token");
                    }
                } else {
                    logger.error("Agent details not found in the agents table.");
                    throw new RuntimeException("Agent details not found.");
                }
            } else {
                logger.error("Password mismatch for email: {}", dto.getEmail());
                throw new RuntimeException("Invalid email or password.");
            }
        } else {
            logger.error("Agent not found for email: {}", dto.getEmail());
            throw new RuntimeException("Invalid email or password.");
        }
    }
  



    @Override
    public Agents getAgentById(int agents_id) {
        logger.info("Fetching agent with ID: {}", agents_id);
        Optional<Agents> agent = agentsRepository.findById(agents_id);
        if (agent.isPresent()) {
            logger.info("Agent found with ID: {}", agents_id);
            return agent.get();
        } else {
            logger.warn("Agent not found with ID: {}", agents_id);
        }
        return null;
    }

    @Override
    public List<Agents> getAllAgents() {
        logger.info("Fetching all agents");
        return agentsRepository.findAll();
    }

    @Override
    public Agents updateAgent(int agents_id, AgentRegistrationDTO agentDTO) {
        logger.info("Attempting to update agent with ID: {}", agents_id);
        Optional<Agents> existingAgent = agentsRepository.findById(agents_id);

        if (existingAgent.isPresent()) {
            Agents agent = existingAgent.get();
            agent.setFull_name(agentDTO.getFull_name());
            agent.setEmail(agentDTO.getEmail());
            agent.setContactno(agentDTO.getContactno());
            agent.setOffice_location(agentDTO.getOffice_location());
            agent.setStatus(agentDTO.getStatus());
            agent.setRole(agentDTO.getRole());
            logger.info("Agent updated successfully with ID: {}", agents_id);
            return agentsRepository.save(agent);
        } else {
            logger.error("Agent not found with ID: {}", agents_id);
        }
        return null;
    }

    @Override
    public String deleteAgent(int agents_id) {
        logger.info("Attempting to delete agent with ID: {}", agents_id);
        return agentsRepository.findById(agents_id)
            .map(agent -> {
                agentsRepository.delete(agent);
                logger.info("Agent deleted successfully with ID: {}", agents_id);
                return "Agent deleted successfully";
            })
            .orElse("Agent not found");
    }
}
